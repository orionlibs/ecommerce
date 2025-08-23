package com.hybris.datahub.client;

public class DataHubBlockedException extends RuntimeException
{
    public DataHubBlockedException()
    {
        this("Request denied: DataHub is busy and cannot be interrupted. Try later.");
    }


    public DataHubBlockedException(String msg)
    {
        super(msg);
    }
}
