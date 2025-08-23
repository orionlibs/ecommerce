package de.hybris.platform.embeddedserver.api;

public class EmbeddedServerInstantationException extends RuntimeException
{
    private static final long serialVersionUID = 1L;


    public EmbeddedServerInstantationException(String message)
    {
        super(message);
    }


    public EmbeddedServerInstantationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
