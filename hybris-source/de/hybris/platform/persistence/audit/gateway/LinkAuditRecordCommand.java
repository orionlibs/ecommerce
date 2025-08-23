package de.hybris.platform.persistence.audit.gateway;

import de.hybris.platform.persistence.audit.AuditType;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;

public class LinkAuditRecordCommand implements AuditRecordCommand
{
    private final TypeAuditRecordCommand typeAuditRecordCommand;
    private final Long sourcePk;
    private final Long targetPk;
    private final Long languagePk;


    private LinkAuditRecordCommand(Builder builder)
    {
        this.typeAuditRecordCommand = builder.auditRecordCommand;
        this.sourcePk = (builder.sourcePk == null) ? null : builder.sourcePk.getLong();
        this.targetPk = (builder.targetPk == null) ? null : builder.targetPk.getLong();
        this.languagePk = (builder.languagePk == null) ? null : builder.languagePk.getLong();
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public Long getPk()
    {
        return this.typeAuditRecordCommand.getPk();
    }


    public String getType()
    {
        return this.typeAuditRecordCommand.getType();
    }


    public Long getTypePk()
    {
        return this.typeAuditRecordCommand.getTypePk();
    }


    public String getChangingUser()
    {
        return this.typeAuditRecordCommand.getChangingUser();
    }


    public AuditType getAuditType()
    {
        return this.typeAuditRecordCommand.getAuditType();
    }


    public Date getTimestamp()
    {
        return this.typeAuditRecordCommand.getTimestamp();
    }


    public Date getCurrentTimestamp()
    {
        return this.typeAuditRecordCommand.getCurrentTimestamp();
    }


    public Map<String, Object> getPayloadBefore()
    {
        return this.typeAuditRecordCommand.getPayloadBefore();
    }


    public Map<String, Object> getPayloadAfter()
    {
        return this.typeAuditRecordCommand.getPayloadAfter();
    }


    public Long getSourcePk()
    {
        return this.sourcePk;
    }


    public Long getTargetPk()
    {
        return this.targetPk;
    }


    public Long getLanguagePk()
    {
        return this.languagePk;
    }


    public Map<String, Object> getContext()
    {
        return this.typeAuditRecordCommand.getContext();
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("typeAuditRecordCommand", this.typeAuditRecordCommand)
                        .append("sourcePk", this.sourcePk)
                        .append("targetPk", this.targetPk)
                        .append("languagePk", this.languagePk)
                        .toString();
    }
}
