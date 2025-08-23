package de.hybris.platform.persistence.audit.payload;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayloadDeserializer
{
    private static final Logger LOG = LoggerFactory.getLogger(PayloadDeserializer.class);


    public AuditPayload deserialize(String payload)
    {
        try
        {
            return (AuditPayload)(new ObjectMapper()).readValue(payload, AuditPayload.class);
        }
        catch(IOException e)
        {
            LOG.error("Failed to parse audit json log", e);
            return null;
        }
    }
}
