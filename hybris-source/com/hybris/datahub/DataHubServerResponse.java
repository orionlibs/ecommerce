package com.hybris.datahub;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class DataHubServerResponse<T>
{
    public static final String X_COUNT = "X-Count";
    private final Response clientResponse;
    private T entity;


    public DataHubServerResponse(Response clientResponse)
    {
        this.clientResponse = clientResponse;
    }


    public DataHubServerResponse(Response clientResponse, Class<T> expectedClass)
    {
        this.clientResponse = clientResponse;
        this.entity = (T)clientResponse.readEntity(expectedClass);
    }


    public DataHubServerResponse(Response clientResponse, GenericType<T> type)
    {
        this.clientResponse = clientResponse;
        handleResponseErrors(clientResponse);
        this.entity = (T)clientResponse.readEntity(type);
    }


    private static void handleResponseErrors(Response clientResponse)
    {
        if(clientResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode())
        {
            throw new IllegalArgumentException((String)clientResponse.readEntity(String.class));
        }
    }


    public Response getResponse()
    {
        return this.clientResponse;
    }


    public T getEntity()
    {
        return this.entity;
    }


    public long getTotalCount()
    {
        String val = this.clientResponse.getHeaderString("X-Count");
        return (val != null) ? Long.valueOf(val).longValue() : -1L;
    }
}
