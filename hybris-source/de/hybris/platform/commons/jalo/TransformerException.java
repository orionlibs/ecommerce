package de.hybris.platform.commons.jalo;

import de.hybris.platform.jalo.JaloSystemException;

public class TransformerException extends JaloSystemException
{
    public TransformerException(Throwable nested, String message, int errorCode)
    {
        super(nested, message, errorCode);
    }


    public TransformerException(String message, int errorCode)
    {
        super(message, errorCode);
    }


    public TransformerException(Throwable nested, int errorCode)
    {
        super(nested, errorCode);
    }


    public TransformerException(Throwable nested)
    {
        super(nested);
    }


    public TransformerException(String message)
    {
        super(message);
    }
}
