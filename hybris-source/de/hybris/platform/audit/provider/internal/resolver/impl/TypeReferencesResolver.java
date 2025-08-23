package de.hybris.platform.audit.provider.internal.resolver.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AbstractTypedAttribute;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.provider.internal.resolver.ReferencesResolver;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.directpersistence.cache.SLDDataContainerProvider;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

public class TypeReferencesResolver extends AbstractReferencesResolver
{
    private SLDDataContainerProvider sldDataContainerProvider;


    public Collection<ReferencesResolver.ResolveResult> resolve(TypeAuditReportConfig config, Map<AbstractTypedAttribute, AuditTypeContext<AuditRecord>> attributes, Collection<AuditRecord> baseRecords)
    {
        Objects.requireNonNull(baseRecords, "baseRecord is required");
        Objects.requireNonNull(attributes, "attributes is required");
        Map<Type, Set<PK>> referencePks = getReferencePks(baseRecords, attributes);
        List<ReferencesResolver.ResolveResult> result = (List<ReferencesResolver.ResolveResult>)referencePks.entrySet().stream().flatMap(entry -> resolveForType((Type)entry.getKey(), (Set<PK>)entry.getValue())).collect(Collectors.toList());
        return (Collection<ReferencesResolver.ResolveResult>)ImmutableList.copyOf(result);
    }


    private Stream<DefaultResolveResult> resolveForType(Type typeToResolve, Set<PK> pksToResolve)
    {
        Stream<AuditRecord> currentAuditRecords = getCurrentAuditRecords(typeToResolve, pksToResolve);
        Stream<AuditRecord> historicalAuditRecords = getHistoricalAuditRecords(typeToResolve, pksToResolve);
        Map<PK, List<AuditRecord>> auditsForPks = (Map<PK, List<AuditRecord>>)Stream.<AuditRecord>concat(currentAuditRecords, historicalAuditRecords).collect(Collectors.groupingBy(AuditRecord::getPk));
        return auditsForPks.entrySet().stream()
                        .map(entry -> new DefaultResolveResult((PK)entry.getKey(), typeToResolve.getCode(), (Collection)entry.getValue()));
    }


    private Stream<AuditRecord> getHistoricalAuditRecords(Type typeToResolve, Set<PK> pksToResolve)
    {
        AuditSearchQuery query = AuditSearchQuery.forType(typeToResolve.getCode()).withPkSearchRules((PK[])Iterables.toArray(pksToResolve, PK.class)).build();
        return this.readAuditGateway.search(query);
    }


    private Stream<AuditRecord> getCurrentAuditRecords(Type typeToResolve, Set<PK> pksToResolve)
    {
        List<SLDDataContainer> rootTypeSldDataContainer = this.sldDataContainerProvider.getAll(new ArrayList<>(pksToResolve));
        return itemsToAuditRecords(rootTypeSldDataContainer, typeToResolve);
    }


    private static Map<Type, Set<PK>> getReferencePks(Collection<AuditRecord> baseRecord, Map<AbstractTypedAttribute, AuditTypeContext<AuditRecord>> attributes)
    {
        Objects.requireNonNull(baseRecord, "baseRecord is required");
        Objects.requireNonNull(attributes, "attributes is required");
        Map<Type, Set<PK>> referencesPks = new HashMap<>();
        for(Iterator<AuditRecord> iterator = baseRecord.iterator(); iterator.hasNext(); )
        {
            AuditRecord auditRecord = iterator.next();
            attributes.entrySet().stream().filter(e -> ((AuditTypeContext)e.getValue()).getBasePKs().contains(auditRecord.getPk())).forEach(e -> {
                Collection<PK> pks = getReferencePks(auditRecord, ((AbstractTypedAttribute)e.getKey()).getResolvesBy().getExpression());
                if(!pks.isEmpty())
                {
                    ((Set<PK>)referencesPks.computeIfAbsent(((AbstractTypedAttribute)e.getKey()).getType(), ())).addAll(pks);
                }
            });
        }
        return referencesPks;
    }


    private static Collection<PK> getReferencePks(AuditRecord baseRecord, String expression)
    {
        Object value = baseRecord.getAttributeBeforeOperation(expression.toLowerCase());
        if(value instanceof PK)
        {
            return Arrays.asList(new PK[] {(PK)value});
        }
        if(value instanceof List)
        {
            return (List)value;
        }
        if(value instanceof Set)
        {
            return (Set)value;
        }
        if(value instanceof Map)
        {
            List<PK> result = new ArrayList<>();
            for(Object val : ((Map)value).values())
            {
                if(val instanceof ItemPropertyValue)
                {
                    result.add(((ItemPropertyValue)val).getPK());
                    continue;
                }
                if(val instanceof PK)
                {
                    result.add((PK)val);
                }
            }
            return result;
        }
        return Collections.emptyList();
    }


    @Required
    public void setSldDataContainerProvider(SLDDataContainerProvider sldDataContainerProvider)
    {
        this.sldDataContainerProvider = sldDataContainerProvider;
    }
}
