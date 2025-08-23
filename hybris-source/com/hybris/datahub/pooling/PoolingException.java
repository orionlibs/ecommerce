package com.hybris.datahub.pooling;

public class PoolingException extends Exception
{
    private static final long serialVersionUID = 1115905391150592776L;


    public PoolingException()
    {
    }


    public PoolingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public PoolingException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public PoolingException(String message)
    {
        super(message);
    }


    public PoolingException(Throwable cause)
    {
        super(cause);
    }
}
