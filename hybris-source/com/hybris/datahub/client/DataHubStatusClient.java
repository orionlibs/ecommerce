package com.hybris.datahub.client;

import com.hybris.datahub.lifecycle.DataHubState;
import com.hybris.datahub.log.Log;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;

public class DataHubStatusClient extends RestClient
{
    private static final Logger LOGGER = (Logger)Log.getLogger(DataHubStatusClient.class);


    public DataHubStatusClient()
    {
        this(null, "");
    }


    public DataHubStatusClient(String uri)
    {
        this(null, uri);
    }


    public DataHubStatusClient(ClientConfiguration cfg, String baseUri)
    {
        super(cfg, baseUri);
    }


    public boolean isDataHubRunning()
    {
        boolean isRunning = true;
        try
        {
            Response response = resource("/status").accept(new String[] {"text/plain"}).get();
            int statusCode = response.getStatus();
            String statusMessage = (String)response.readEntity(String.class);
            if(!isValidStatus(statusCode, statusMessage))
            {
                LOGGER.warn("Data Hub @ {} returned unexpected status. Status Code: {}, Status Message: {}", new Object[] {getBaseApiUrl(), Integer.valueOf(statusCode), statusMessage});
                isRunning = false;
            }
        }
        catch(RuntimeException ex)
        {
            LOGGER.warn("Data Hub @ {} is not running because: {}", new Object[] {getBaseApiUrl(), ex.getMessage(), ex});
            isRunning = false;
        }
        return isRunning;
    }


    private static boolean isValidStatus(int statusCode, String statusMessage)
    {
        return (Response.Status.OK.getStatusCode() == statusCode && EnumUtils.isValidEnum(DataHubState.class, statusMessage));
    }
}
