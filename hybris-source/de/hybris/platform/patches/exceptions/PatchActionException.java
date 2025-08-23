package de.hybris.platform.patches.exceptions;

public class PatchActionException extends RuntimeException
{
    public PatchActionException(Throwable cause)
    {
        super(cause);
    }


    public PatchActionException(String message)
    {
        super(message);
    }


    public PatchActionException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
