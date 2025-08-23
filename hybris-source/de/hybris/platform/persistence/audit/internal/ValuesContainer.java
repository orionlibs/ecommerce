package de.hybris.platform.persistence.audit.internal;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.persistence.property.TypeInfoMap;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ValuesContainer
{
    private final PK pk;
    private final PK typePk;
    private final AuditType auditType;
    private final Map<String, Object> valuesBefore;
    private final Map<String, Object> valuesAfter;
    private final TypeInfoMap typeInfoMap;
    private final Map<String, Object> context;


    private ValuesContainer(Builder builder)
    {
        this.pk = builder.pk;
        this.typePk = builder.typePk;
        this.auditType = builder.auditType;
        this.valuesBefore = builder.valuesBefore;
        this.valuesAfter = builder.valuesAfter;
        this.context = builder.context;
        this.typeInfoMap = getPersistenceManager().getPersistenceInfo(this.typePk);
    }


    PersistenceManager getPersistenceManager()
    {
        return Registry.getCurrentTenant().getPersistenceManager();
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public PK getPk()
    {
        return this.pk;
    }


    public PK getTypePk()
    {
        return this.typePk;
    }


    public String getTypeCode()
    {
        return this.typeInfoMap.getCode();
    }


    public boolean isRelationType()
    {
        return this.typeInfoMap.isRelationType();
    }


    public AuditType getAuditType()
    {
        return this.auditType;
    }


    public Map<String, Object> getValuesBefore()
    {
        return (this.valuesBefore == null) ? Collections.<String, Object>emptyMap() : (Map<String, Object>)ImmutableMap.copyOf(this.valuesBefore);
    }


    public Map<String, Object> getValuesAfter()
    {
        return (this.valuesAfter == null) ? Collections.<String, Object>emptyMap() : (Map<String, Object>)ImmutableMap.copyOf(this.valuesAfter);
    }


    public Map<String, Object> getContext()
    {
        return (this.context == null) ? Collections.<String, Object>emptyMap() : (Map<String, Object>)ImmutableMap.copyOf(this.context);
    }


    public boolean isForOperation(AuditType operationType)
    {
        return (this.auditType == operationType);
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("pk", this.pk)
                        .append("typePk", this.typePk)
                        .append("auditType", this.auditType)
                        .append("values", this.valuesBefore)
                        .toString();
    }
}
