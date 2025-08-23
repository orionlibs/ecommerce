package de.hybris.platform.persistence.audit.gateway;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.AuditType;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;

public class LinkAuditRecord implements AuditRecord
{
    private final AuditRecord auditRecord;
    private final LinkSide parentSide;
    private final PK sourcePk;
    private final PK targetPk;


    public LinkAuditRecord(Builder builder)
    {
        this.auditRecord = builder.auditRecord;
        this.parentSide = builder.parentSide;
        this.sourcePk = builder.sourcePk;
        this.targetPk = builder.targetPk;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public PK getParentPk()
    {
        return (this.parentSide == LinkSide.TARGET) ? this.targetPk : this.sourcePk;
    }


    public PK getChildPk()
    {
        return (this.parentSide == LinkSide.TARGET) ? this.sourcePk : this.targetPk;
    }


    public PK getSourcePk()
    {
        return this.sourcePk;
    }


    public PK getTargetPk()
    {
        return this.targetPk;
    }


    public boolean isLink()
    {
        return true;
    }


    public Long getVersion()
    {
        return this.auditRecord.getVersion();
    }


    public PK getPk()
    {
        return this.auditRecord.getPk();
    }


    public String getType()
    {
        return this.auditRecord.getType();
    }


    public PK getTypePk()
    {
        return this.auditRecord.getTypePk();
    }


    public String getChangingUser()
    {
        return this.auditRecord.getChangingUser();
    }


    public AuditType getAuditType()
    {
        return this.auditRecord.getAuditType();
    }


    public Object getAttributeBeforeOperation(String key)
    {
        return this.auditRecord.getAttributeBeforeOperation(key);
    }


    public Object getAttributeBeforeOperation(String key, String langIsoCode)
    {
        return this.auditRecord.getAttributeBeforeOperation(key, langIsoCode);
    }


    public Map<String, Object> getAttributesBeforeOperation()
    {
        return this.auditRecord.getAttributesBeforeOperation();
    }


    public Object getAttributeAfterOperation(String key)
    {
        return this.auditRecord.getAttributeAfterOperation(key);
    }


    public Object getAttributeAfterOperation(String key, String langIsoCode)
    {
        return this.auditRecord.getAttributeAfterOperation(key, langIsoCode);
    }


    public Map<String, Object> getAttributesAfterOperation()
    {
        return this.auditRecord.getAttributesAfterOperation();
    }


    public Map<String, Object> getAttributesBeforeOperation(String langIsoCode)
    {
        return this.auditRecord.getAttributesBeforeOperation(langIsoCode);
    }


    public Map<String, Object> getAttributesAfterOperation(String langIsoCode)
    {
        return this.auditRecord.getAttributesAfterOperation(langIsoCode);
    }


    public Date getTimestamp()
    {
        return this.auditRecord.getTimestamp();
    }


    public Date getCurrentTimestamp()
    {
        return this.auditRecord.getCurrentTimestamp();
    }


    public Map<String, Object> getContext()
    {
        return this.auditRecord.getContext();
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        LinkAuditRecord that = (LinkAuditRecord)o;
        if((this.auditRecord != null) ? !this.auditRecord.equals(that.auditRecord) : (that.auditRecord != null))
        {
            return false;
        }
        return (this.parentSide == that.parentSide);
    }


    public int hashCode()
    {
        int result = (this.auditRecord != null) ? this.auditRecord.hashCode() : 0;
        result = 31 * result + ((this.parentSide != null) ? this.parentSide.hashCode() : 0);
        return result;
    }


    public String toString()
    {
        return (new ToStringBuilder(this)).append("id", getVersion()).append("pk", getPk()).append("type", getType())
                        .append("parentPk", getParentPk()).append("childPk", getChildPk()).toString();
    }
}
