package de.hybris.platform.persistence.audit.gateway.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.persistence.audit.payload.PayloadDeserializer;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractReadAuditGateway implements ReadAuditGateway
{
    private PayloadDeserializer payloadDeserializer;


    protected AuditPayload deserialize(String payload)
    {
        return this.payloadDeserializer.deserialize(payload);
    }


    protected PK toPK(Object pk)
    {
        if(pk instanceof Long)
        {
            return PK.fromLong(((Long)pk).longValue());
        }
        if(pk instanceof String)
        {
            return PK.parse((String)pk);
        }
        return null;
    }


    protected AuditType toAuditType(Object auditType)
    {
        if(auditType instanceof Long)
        {
            return AuditType.toAuditType(((Long)auditType).intValue());
        }
        if(auditType instanceof String)
        {
            return AuditType.toAuditType(Long.parseLong((String)auditType));
        }
        return null;
    }


    @Required
    public void setPayloadDeserializer(PayloadDeserializer payloadDeserializer)
    {
        this.payloadDeserializer = payloadDeserializer;
    }
}
