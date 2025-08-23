package de.hybris.platform.audit.provider.internal.resolver.impl;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.audit.provider.internal.resolver.ReferencesResolver;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import java.util.Collection;

public class DefaultResolveResult implements ReferencesResolver.ResolveResult
{
    private final PK itemPk;
    private final String typeCode;
    private final Collection<AuditRecord> records;


    public DefaultResolveResult(PK itemPk, String typeCode, Collection<AuditRecord> records)
    {
        this.itemPk = itemPk;
        this.typeCode = typeCode;
        this.records = records;
    }


    public PK getItemPk()
    {
        return this.itemPk;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public Collection<AuditRecord> getRecords()
    {
        return (Collection<AuditRecord>)ImmutableList.copyOf(this.records);
    }
}
