package de.hybris.platform.persistence.audit.gateway.impl;

import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.audit.payload.PayloadSerializer;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractWriteAuditGateway implements WriteAuditGateway
{
    private PayloadSerializer payloadSerializer;


    protected String serialize(Map<String, Object> payload)
    {
        return this.payloadSerializer.serialize(payload);
    }


    @Required
    public void setPayloadSerializer(PayloadSerializer payloadSerializer)
    {
        this.payloadSerializer = payloadSerializer;
    }
}
