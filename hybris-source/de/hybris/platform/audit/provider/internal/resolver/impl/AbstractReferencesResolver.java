package de.hybris.platform.audit.provider.internal.resolver.impl;

import com.google.common.collect.Iterables;
import de.hybris.platform.audit.internal.ModelAuditRecordConverter;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.provider.internal.resolver.ReferencesResolver;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.LinkAuditRecord;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractReferencesResolver implements ReferencesResolver
{
    protected ModelAuditRecordConverter converter;
    protected ReadAuditGateway readAuditGateway;


    protected Stream<AuditRecord> itemsToAuditRecords(Collection<SLDDataContainer> containers, Type typeToResolve)
    {
        return itemsToAuditRecords(containers, typeToResolve.getCode());
    }


    protected Stream<AuditRecord> itemsToAuditRecords(Collection<SLDDataContainer> containers, String typeToResolve)
    {
        if(containers == null)
        {
            return Stream.empty();
        }
        return containers.stream().filter(sldDataContainer -> !Objects.isNull(sldDataContainer))
                        .map(i -> this.converter.toAuditRecord(i, typeToResolve));
    }


    protected Stream<AuditRecord> getAuditRecords(Type typeToResolve, Collection<PK> pks)
    {
        AuditSearchQuery query = AuditSearchQuery.forType(typeToResolve.getCode()).withPkSearchRules((PK[])Iterables.toArray(pks, PK.class)).build();
        return this.readAuditGateway.search(query);
    }


    protected List<LinkAuditRecord> getAuditRecordsForLinkByTarget(PK baseRecordPk, String linkTypeCode)
    {
        AuditSearchQuery query = AuditSearchQuery.forLink(linkTypeCode).withPk(baseRecordPk).buildForTarget();
        return (List<LinkAuditRecord>)this.readAuditGateway.search(query).collect(Collectors.toList());
    }


    protected List<LinkAuditRecord> getAuditRecordsForLinkBySource(PK baseRecordPk, String linkTypeCode)
    {
        AuditSearchQuery query = AuditSearchQuery.forLink(linkTypeCode).withPk(baseRecordPk).buildForSource();
        return (List<LinkAuditRecord>)this.readAuditGateway.search(query).collect(Collectors.toList());
    }


    @Required
    public void setConverter(ModelAuditRecordConverter converter)
    {
        this.converter = converter;
    }


    @Required
    public void setReadAuditGateway(ReadAuditGateway readAuditGateway)
    {
        this.readAuditGateway = readAuditGateway;
    }
}
