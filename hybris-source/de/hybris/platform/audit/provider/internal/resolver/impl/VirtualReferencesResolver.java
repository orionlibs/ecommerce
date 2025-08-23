package de.hybris.platform.audit.provider.internal.resolver.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AbstractTypedAttribute;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.provider.internal.resolver.AuditRecordInternalProvider;
import de.hybris.platform.audit.provider.internal.resolver.ReferencesResolver;
import de.hybris.platform.audit.provider.internal.resolver.VirtualReferenceExpressionParser;
import de.hybris.platform.audit.provider.internal.resolver.VirtualReferenceValuesExtractor;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.cache.SLDDataContainerProvider;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

public class VirtualReferencesResolver extends AbstractReferencesResolver
{
    private FlexibleSearchService flexibleSearchService;
    private SLDDataContainerProvider sldDataContainerProvider;
    private VirtualReferenceExpressionParser virtualReferenceExpressionParser;


    public Collection<ReferencesResolver.ResolveResult> resolve(TypeAuditReportConfig config, Map<AbstractTypedAttribute, AuditTypeContext<AuditRecord>> attributes, Collection<AuditRecord> baseRecords)
    {
        Objects.requireNonNull(baseRecords, "baseRecord is required");
        Objects.requireNonNull(attributes, "attributes is required");
        Set<Type> types = (Set<Type>)attributes.keySet().stream().map(AbstractTypedAttribute::getType).collect(Collectors.toSet());
        Preconditions.checkArgument((types.size() == 1), "all attributes should be for the same type but %s found", types
                        .stream().map(Type::getCode).collect(Collectors.joining(",", "[", "]")));
        Type typeToResolve = types.iterator().next();
        Stream<AuditRecord> auditRecords = getAuditRecords(attributes, typeToResolve);
        Map<PK, List<AuditRecord>> auditsForPks = auditRecords.collect((Collector)Collectors.groupingBy(AuditRecord::getPk));
        List<ReferencesResolver.ResolveResult> results = (List<ReferencesResolver.ResolveResult>)auditsForPks.entrySet().stream().map(pkListEntry -> new DefaultResolveResult((PK)pkListEntry.getKey(), typeToResolve.getCode(), (Collection)pkListEntry.getValue())).collect(Collectors.toList());
        return (Collection<ReferencesResolver.ResolveResult>)ImmutableList.copyOf(results);
    }


    private Stream<AuditRecord> getAuditRecords(Map<AbstractTypedAttribute, AuditTypeContext<AuditRecord>> attributes, Type typeToResolve)
    {
        return attributes.entrySet().stream().map(entry -> {
            AbstractTypedAttribute attribute = (AbstractTypedAttribute)entry.getKey();
            String expression = attribute.getResolvesBy().getExpression();
            String qualifier = this.virtualReferenceExpressionParser.getQualifier(expression);
            VirtualReferenceValuesExtractor valueResolver = this.virtualReferenceExpressionParser.getResolver(expression);
            VirtualReferenceInternalProvider virtualReferenceInternalProvider = new VirtualReferenceInternalProvider(this, this.flexibleSearchService, this.sldDataContainerProvider, this.readAuditGateway, typeToResolve.getCode(), qualifier);
            return valueResolver.extractValues((AuditRecordInternalProvider)virtualReferenceInternalProvider, (AuditTypeContext)entry.getValue());
        }).flatMap(Collection::stream);
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
    public void setVirtualReferenceExpressionParser(VirtualReferenceExpressionParser virtualReferenceExpressionParser)
    {
        this.virtualReferenceExpressionParser = virtualReferenceExpressionParser;
    }
}
