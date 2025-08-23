package com.hybris.datahub.process;

public class DataHubProcessingException extends RuntimeException
{
    public DataHubProcessingException()
    {
    }


    public DataHubProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public DataHubProcessingException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public DataHubProcessingException(String message)
    {
        super(message);
    }


    public DataHubProcessingException(Throwable cause)
    {
        super(cause);
    }
}
