package de.hybris.platform.embeddedserver.api;

public class EmbeddedServerLifecycleException extends RuntimeException
{
    private static final long serialVersionUID = 1L;


    public EmbeddedServerLifecycleException(String message)
    {
        super(message);
    }


    public EmbeddedServerLifecycleException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
