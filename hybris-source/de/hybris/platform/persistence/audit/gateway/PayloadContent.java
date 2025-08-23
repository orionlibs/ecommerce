package de.hybris.platform.persistence.audit.gateway;

import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import java.util.Map;

public class PayloadContent
{
    private final AuditPayload payloadBefore;
    private final AuditPayload payloadAfter;
    private final Map<String, Object> context;


    public PayloadContent(AuditPayload payloadBefore, AuditPayload payloadAfter, Map<String, Object> context)
    {
        this.payloadBefore = payloadBefore;
        this.payloadAfter = payloadAfter;
        this.context = context;
    }


    public AuditPayload getPayloadBefore()
    {
        return this.payloadBefore;
    }


    public AuditPayload getPayloadAfter()
    {
        return this.payloadAfter;
    }


    public Map<String, Object> getContext()
    {
        return this.context;
    }
}
