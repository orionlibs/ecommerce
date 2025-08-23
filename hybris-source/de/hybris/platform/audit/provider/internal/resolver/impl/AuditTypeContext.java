package de.hybris.platform.audit.provider.internal.resolver.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.platform.audit.provider.internal.resolver.AuditRecordInternalIndex;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.internal.AuditRecordInternal;
import java.util.Set;

public class AuditTypeContext<T extends AuditRecordInternal>
{
    private final AuditRecordInternalIndex<T> index;
    private final String type;
    private final String baseType;
    private final Set<PK> basePKs;
    private final Set<String> langIsoCodes;


    public AuditTypeContext(AuditRecordInternalIndex<T> index, String type, String baseType, PK basePK, Set<String> langIsoCodes)
    {
        this.index = index;
        this.type = type;
        this.baseType = baseType;
        this.basePKs = (Set<PK>)ImmutableSet.of(basePK);
        this.langIsoCodes = (Set<String>)ImmutableSet.copyOf(langIsoCodes);
    }


    public AuditTypeContext(AuditRecordInternalIndex<T> index, String type, String baseType, Set<PK> basePKs, Set<String> langIsoCodes)
    {
        this.index = index;
        this.type = type;
        this.baseType = baseType;
        this.basePKs = (Set<PK>)ImmutableSet.copyOf(basePKs);
        this.langIsoCodes = (Set<String>)ImmutableSet.copyOf(langIsoCodes);
    }


    public Set<T> getPayloads(PK pk)
    {
        return this.index.getRecords(pk);
    }


    public Set<T> getPayloads(String type)
    {
        return this.index.getRecords(type);
    }


    public Set<T> getPayloads(String type, PK pk)
    {
        return this.index.getRecords(type, pk);
    }


    public Set<T> getPayloadsForBasePKs()
    {
        return (Set<T>)getBasePKs().stream().map(this::getPayloads).collect(java.util.HashSet::new, Set::addAll, Set::addAll);
    }


    public String getType()
    {
        return this.type;
    }


    public String getBaseType()
    {
        return this.baseType;
    }


    public Set<PK> getBasePKs()
    {
        return this.basePKs;
    }


    public Set<String> getLangIsoCodes()
    {
        return this.langIsoCodes;
    }
}
