package de.hybris.platform.persistence.audit.gateway;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.AuditType;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ModelAuditRecord implements AuditRecord
{
    private final Long id;
    private final PK pk;
    private final String type;
    private final PK typePk;
    private final String changingUser;
    private final AuditType auditType;
    private final Date timestamp;
    private final Map<String, Object> attributes;
    private final Map<String, Map<String, Object>> localizedAttributes;
    private final Map<String, Object> context;


    private ModelAuditRecord(Builder builder)
    {
        this.id = Long.valueOf(0L);
        this.pk = builder.pk;
        this.type = builder.type;
        this.typePk = builder.typePk;
        this.changingUser = builder.changingUser;
        this.auditType = builder.auditType;
        this.timestamp = builder.timestamp;
        this.attributes = builder.attributes;
        this.localizedAttributes = builder.localizedAttributes;
        this.context = builder.context;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public Long getVersion()
    {
        return this.id;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public String getType()
    {
        return this.type;
    }


    public PK getTypePk()
    {
        return this.typePk;
    }


    public String getChangingUser()
    {
        return this.changingUser;
    }


    public AuditType getAuditType()
    {
        return this.auditType;
    }


    public Map<String, Object> getAttributesBeforeOperation()
    {
        return Collections.unmodifiableMap(this.attributes);
    }


    public Map<String, Object> getAttributesAfterOperation()
    {
        return getAttributesBeforeOperation();
    }


    public Map<String, Object> getAttributesBeforeOperation(String langIsoCode)
    {
        Objects.requireNonNull(langIsoCode, "langIsoCode is required");
        Map<String, Object> result = new HashMap<>();
        for(Map.Entry<String, Map<String, Object>> entry : this.localizedAttributes.entrySet())
        {
            String qualifier = entry.getKey();
            Map<String, Object> valueMap = entry.getValue();
            if(MapUtils.isNotEmpty(valueMap))
            {
                Objects.requireNonNull(langIsoCode);
                Predicate<String> equalsIgnoreCase = langIsoCode::equalsIgnoreCase;
                result.put(qualifier, Maps.filterKeys(valueMap, equalsIgnoreCase));
            }
        }
        return result;
    }


    public Map<String, Object> getAttributesAfterOperation(String langIsoCode)
    {
        return getAttributesBeforeOperation(langIsoCode);
    }


    public Object getAttributeBeforeOperation(String key)
    {
        Object nonLocalizedAttr = this.attributes.get(key);
        return (nonLocalizedAttr != null) ? nonLocalizedAttr : this.localizedAttributes.get(key);
    }


    public Object getAttributeBeforeOperation(String key, String langIsoCode)
    {
        return getAttributesBeforeOperation(langIsoCode).get(key);
    }


    public Object getAttributeAfterOperation(String key)
    {
        return getAttributeBeforeOperation(key);
    }


    public Object getAttributeAfterOperation(String key, String langIsoCode)
    {
        return getAttributeBeforeOperation(key, langIsoCode);
    }


    public Date getTimestamp()
    {
        return this.timestamp;
    }


    public Date getCurrentTimestamp()
    {
        return null;
    }


    public Map<String, Object> getContext()
    {
        return this.context;
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("id", this.id)
                        .append("pk", this.pk)
                        .append("type", this.type)
                        .append("typePk", this.typePk)
                        .append("changingUser", this.changingUser)
                        .append("auditType", this.auditType)
                        .append("timestamp", this.timestamp)
                        .toString();
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof ModelAuditRecord))
        {
            return false;
        }
        ModelAuditRecord that = (ModelAuditRecord)o;
        return (Objects.equals(this.id, that.id) && Objects.equals(getPk(), that.getPk()) && Objects.equals(getType(), that.getType()) &&
                        Objects.equals(getTypePk(), that.getTypePk()) && Objects.equals(getChangingUser(), that.getChangingUser()) &&
                        getAuditType() == that.getAuditType() && Objects.equals(getTimestamp(), that.getTimestamp()) &&
                        Objects.equals(getCurrentTimestamp(), that.getCurrentTimestamp()));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.id, getPk(), getType(), getTypePk(), getChangingUser(), getAuditType(), getTimestamp(),
                        getCurrentTimestamp()});
    }
}
