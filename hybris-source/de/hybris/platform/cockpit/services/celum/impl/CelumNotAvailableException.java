package de.hybris.platform.cockpit.services.celum.impl;

public class CelumNotAvailableException extends Exception
{
    public CelumNotAvailableException(String message)
    {
        super(message);
    }


    public CelumNotAvailableException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public CelumNotAvailableException(Throwable cause)
    {
        super(cause);
    }
}
