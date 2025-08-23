package de.hybris.platform.oauth2.jwt.exceptions;

public class KeyStoreProcessingException extends Exception
{
    public KeyStoreProcessingException(String msg)
    {
        super(msg);
    }


    public KeyStoreProcessingException(String msg, Exception nested)
    {
        super(msg, nested);
    }
}
