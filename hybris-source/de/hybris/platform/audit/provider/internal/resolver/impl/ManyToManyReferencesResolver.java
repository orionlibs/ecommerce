package de.hybris.platform.audit.provider.internal.resolver.impl;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AbstractTypedAttribute;
import de.hybris.platform.audit.internal.config.RelationAttribute;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.provider.internal.resolver.ReferencesResolver;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.directpersistence.cache.SLDDataContainerProvider;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.LinkAuditRecord;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

public class ManyToManyReferencesResolver extends AbstractReferencesResolver
{
    private static final String SELECT_AUDIT_LINK_RECORDS_FS_QUERY = "SELECT '{'PK'}' FROM '{'{0}'}' WHERE '{'{1}'}' IN (?pk)";
    private FlexibleSearchService flexibleSearchService;
    private SLDDataContainerProvider sldDataContainerProvider;
    private TypeService typeService;


    public Collection<ReferencesResolver.ResolveResult> resolve(TypeAuditReportConfig config, Map<AbstractTypedAttribute, AuditTypeContext<AuditRecord>> attributes, Collection<AuditRecord> baseRecords)
    {
        Objects.requireNonNull(baseRecords, "baseRecord is required");
        Objects.requireNonNull(attributes, "attributes is required");
        List<ReferencesResolver.ResolveResult> result = new ArrayList<>();
        for(Map.Entry<AbstractTypedAttribute, AuditTypeContext<AuditRecord>> entry : attributes.entrySet())
        {
            AbstractTypedAttribute attribute = entry.getKey();
            Set<PK> basePks = ((AuditTypeContext)entry.getValue()).getBasePKs();
            String baseType = (String)baseRecords.stream().findFirst().map(AuditRecord::getType).orElseThrow(() -> new IllegalArgumentException("baseRecords should contain at least one element"));
            String linkTypeCode = ((RelationAttribute)attribute).getRelation();
            LinkAuditRecord.LinkSide parentSide = getParentSide(linkTypeCode, baseType);
            List<LinkAuditRecord> linkAuditRecords = getLinkAuditRecords(basePks, parentSide, linkTypeCode);
            result.addAll(linkAuditRecordsToResolveResult(linkTypeCode, linkAuditRecords));
            Set<PK> targetPks = (Set<PK>)linkAuditRecords.stream().map(LinkAuditRecord::getChildPk).distinct().collect(Collectors.toSet());
            result.addAll(getTargetAuditRecords(attribute.getType(), targetPks));
        }
        return (Collection<ReferencesResolver.ResolveResult>)ImmutableList.copyOf(result);
    }


    private LinkAuditRecord.LinkSide getParentSide(String linkTypeCode, String parentType)
    {
        TypeModel baseRecordType = this.typeService.getTypeForCode(parentType);
        String linkSourceAttributeTypeCode = this.typeService.getAttributeDescriptor(linkTypeCode, "source").getAttributeType().getCode();
        String linkTargetAttributeTypeCode = this.typeService.getAttributeDescriptor(linkTypeCode, "target").getAttributeType().getCode();
        if(linkSourceAttributeTypeCode.equals(baseRecordType.getCode()))
        {
            return LinkAuditRecord.LinkSide.SOURCE;
        }
        if(linkTargetAttributeTypeCode.equals(baseRecordType.getCode()))
        {
            return LinkAuditRecord.LinkSide.TARGET;
        }
        if(baseRecordType instanceof ComposedTypeModel)
        {
            ComposedTypeModel testedType = ((ComposedTypeModel)baseRecordType).getSuperType();
            while(testedType != null)
            {
                if(linkSourceAttributeTypeCode.equals(testedType.getCode()))
                {
                    return LinkAuditRecord.LinkSide.SOURCE;
                }
                if(linkTargetAttributeTypeCode.equals(testedType.getCode()))
                {
                    return LinkAuditRecord.LinkSide.TARGET;
                }
                testedType = testedType.getSuperType();
            }
        }
        throw new IllegalStateException("could not find relation parent side");
    }


    private List<LinkAuditRecord> getLinkAuditRecords(Set<PK> baseRecord, LinkAuditRecord.LinkSide parentSide, String linkTypeCode)
    {
        return (List<LinkAuditRecord>)Stream.<LinkAuditRecord>concat(getCurrentLinkAuditRecords(baseRecord, linkTypeCode, parentSide),
                        getHistoricalLinkAuditRecords(baseRecord, linkTypeCode, parentSide)).collect(Collectors.toList());
    }


    private List<ReferencesResolver.ResolveResult> linkAuditRecordsToResolveResult(String relationTypeCode, List<LinkAuditRecord> linkRecords)
    {
        return (List<ReferencesResolver.ResolveResult>)linkRecords.stream()
                        .map(lr -> new DefaultResolveResult(lr.getParentPk(), relationTypeCode, Collections.singleton(lr)))
                        .collect(Collectors.toList());
    }


    private List<ReferencesResolver.ResolveResult> getTargetAuditRecords(Type typeToResolve, Set<PK> targetPks)
    {
        if(targetPks.isEmpty())
        {
            return Collections.emptyList();
        }
        Stream<? extends AuditRecord> currentAuditRecords = itemsToAuditRecords(this.sldDataContainerProvider
                        .getAll(new ArrayList<>(targetPks)), typeToResolve);
        Stream<? extends AuditRecord> historicalAuditRecords = getAuditRecords(typeToResolve, targetPks);
        return (List<ReferencesResolver.ResolveResult>)((Map)Stream.<AuditRecord>concat(currentAuditRecords, historicalAuditRecords).collect(Collectors.groupingBy(AuditRecord::getPk))).entrySet()
                        .stream()
                        .map(pkListEntry -> new DefaultResolveResult((PK)pkListEntry.getKey(), typeToResolve.getCode(), (Collection)pkListEntry.getValue()))
                        .collect(Collectors.toList());
    }


    private Stream<LinkAuditRecord> getHistoricalLinkAuditRecords(Set<PK> baseRecordPk, String linkTypeCode, LinkAuditRecord.LinkSide parentSide)
    {
        if(parentSide == LinkAuditRecord.LinkSide.SOURCE)
        {
            return baseRecordPk.stream().flatMap(pk -> getAuditRecordsForLinkBySource(pk, linkTypeCode).stream());
        }
        return baseRecordPk.stream().flatMap(pk -> getAuditRecordsForLinkByTarget(pk, linkTypeCode).stream());
    }


    private Stream<LinkAuditRecord> getCurrentLinkAuditRecords(Set<PK> parentPk, String typeCode, LinkAuditRecord.LinkSide parentSide)
    {
        String parentSideAttribute = getParentSideAttribute(parentSide);
        FlexibleSearchQuery query = new FlexibleSearchQuery(MessageFormat.format("SELECT '{'PK'}' FROM '{'{0}'}' WHERE '{'{1}'}' IN (?pk)", new Object[] {typeCode, parentSideAttribute}));
        query.addQueryParameter("pk", parentPk);
        query.setResultClassList(Collections.singletonList(PK.class));
        SearchResult<PK> searchResult = this.flexibleSearchService.search(query);
        return this.sldDataContainerProvider.getAll(searchResult.getResult()).stream()
                        .map(sldDataContainer -> this.converter.toLinkAuditRecord(sldDataContainer, typeCode, parentSide));
    }


    private static String getParentSideAttribute(LinkAuditRecord.LinkSide parentSide)
    {
        return (parentSide == LinkAuditRecord.LinkSide.SOURCE) ? "source" : "target";
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setSldDataContainerProvider(SLDDataContainerProvider sldDataContainerProvider)
    {
        this.sldDataContainerProvider = sldDataContainerProvider;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
