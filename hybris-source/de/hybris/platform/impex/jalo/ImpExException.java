package de.hybris.platform.impex.jalo;

import de.hybris.platform.jalo.JaloBusinessException;

public class ImpExException extends JaloBusinessException
{
    public ImpExException(Throwable nested, String message, int errorCode)
    {
        super(nested, message, errorCode);
    }


    public ImpExException(String message, int errorCode)
    {
        super(message, errorCode);
    }


    public ImpExException(Throwable nested, int errorCode)
    {
        super(nested, errorCode);
    }


    public ImpExException(Throwable nested)
    {
        super(nested);
    }


    public ImpExException(String message)
    {
        super(message);
    }
}
