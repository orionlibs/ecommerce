package de.hybris.platform.persistence.audit.gateway;

import de.hybris.platform.persistence.audit.AuditType;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TypeAuditRecordCommand implements AuditRecordCommand
{
    private final Long pk;
    private final String type;
    private final Long typePk;
    private final String changingUser;
    private final AuditType auditType;
    private final Date timestamp;
    private final Date currentTimestamp;
    private final Map<String, Object> payloadBefore;
    private final Map<String, Object> payloadAfter;
    private final Map<String, Object> context;


    private TypeAuditRecordCommand(Builder builder)
    {
        this.pk = (builder.pk == null) ? null : builder.pk.getLong();
        this.type = builder.type;
        this.typePk = (builder.typePk == null) ? null : builder.typePk.getLong();
        this.changingUser = builder.changingUser;
        this.auditType = builder.auditType;
        this.timestamp = builder.timestamp;
        this.currentTimestamp = builder.currentTimestamp;
        this.payloadBefore = builder.payloadBefore;
        this.payloadAfter = builder.payloadAfter;
        this.context = builder.context;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public Long getPk()
    {
        return this.pk;
    }


    public String getType()
    {
        return this.type;
    }


    public Long getTypePk()
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


    public Date getTimestamp()
    {
        return this.timestamp;
    }


    public Date getCurrentTimestamp()
    {
        return this.currentTimestamp;
    }


    public Map<String, Object> getPayloadBefore()
    {
        return this.payloadBefore;
    }


    public Map<String, Object> getPayloadAfter()
    {
        return this.payloadAfter;
    }


    public Map<String, Object> getContext()
    {
        return this.context;
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("pk", this.pk)
                        .append("type", this.type)
                        .append("typePk", this.typePk)
                        .append("changingUser", this.changingUser)
                        .append("auditType", this.auditType)
                        .append("timestamp", this.timestamp)
                        .append("currentTimestamp", this.currentTimestamp)
                        .append("payloadBefore", this.payloadBefore)
                        .append("payloadAfter", this.payloadAfter)
                        .append("context", this.context)
                        .toString();
    }
}
