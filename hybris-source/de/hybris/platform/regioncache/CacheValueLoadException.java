package de.hybris.platform.regioncache;

public class CacheValueLoadException extends RuntimeException
{
    public CacheValueLoadException(String message)
    {
        super(message);
    }


    public CacheValueLoadException(String message, Exception exc)
    {
        super(message, exc);
    }
}
