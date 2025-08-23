package com.hybris.datahub.client;

import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataHubVersionClient extends RestClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DataHubVersionClient.class);


    public DataHubVersionClient()
    {
        this(null, "");
    }


    public DataHubVersionClient(String uri)
    {
        this(null, uri);
    }


    public DataHubVersionClient(ClientConfiguration cfg, String baseUri)
    {
        super(cfg, baseUri);
    }


    public String getVersion()
    {
        int statusCode;
        String version;
        try
        {
            Response response = resource("/version").accept(new String[] {"text/plain"}).get();
            statusCode = response.getStatus();
            version = (String)response.readEntity(String.class);
        }
        catch(Exception ex)
        {
            LOGGER.warn("Data Hub @ {} returned unexpected response. Message: {}", getBaseApiUrl(), ex.getMessage());
            throw new DataHubClientException("Data Hub @ " + getBaseApiUrl() + " returned unexpected response.", ex);
        }
        if(!isValidVersion(statusCode, version))
        {
            LOGGER.warn("The Data Hub @ {} returned an invalid version. Status Code: {}, Version: {}", new Object[] {getBaseApiUrl(), Integer.valueOf(statusCode), version});
            throw new DataHubClientException("The Data Hub @ " + getBaseApiUrl() + " returned an invalid version. Status Code: " + statusCode + ", Version: " + version);
        }
        return version;
    }


    private static boolean isValidVersion(int status, String version)
    {
        return (Response.Status.OK.getStatusCode() == status &&
                        StringUtils.contains(version, "Data Hub version:"));
    }
}
