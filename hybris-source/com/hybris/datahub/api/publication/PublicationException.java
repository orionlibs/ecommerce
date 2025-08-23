package com.hybris.datahub.api.publication;

public class PublicationException extends Exception
{
    private static final long serialVersionUID = 6449282884479488266L;


    public PublicationException()
    {
    }


    public PublicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public PublicationException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public PublicationException(String message)
    {
        super(message);
    }


    public PublicationException(Throwable cause)
    {
        super(cause);
    }
}
