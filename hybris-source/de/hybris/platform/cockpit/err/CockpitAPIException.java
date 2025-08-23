package de.hybris.platform.cockpit.err;

public class CockpitAPIException extends RuntimeException
{
    public CockpitAPIException()
    {
    }


    public CockpitAPIException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public CockpitAPIException(String message)
    {
        super(message);
    }


    public CockpitAPIException(Throwable cause)
    {
        super(cause);
    }
}
