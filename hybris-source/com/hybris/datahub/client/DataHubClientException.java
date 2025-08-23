package com.hybris.datahub.client;

public class DataHubClientException extends RuntimeException
{
    public DataHubClientException(String message)
    {
        super(message);
    }


    public DataHubClientException(String msg, Exception cause)
    {
        super(msg, cause);
    }
}
