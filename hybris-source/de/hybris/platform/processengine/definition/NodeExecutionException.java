package de.hybris.platform.processengine.definition;

public class NodeExecutionException extends Exception
{
    public NodeExecutionException()
    {
    }


    public NodeExecutionException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public NodeExecutionException(String message)
    {
        super(message);
    }


    public NodeExecutionException(Throwable cause)
    {
        super(cause);
    }
}
