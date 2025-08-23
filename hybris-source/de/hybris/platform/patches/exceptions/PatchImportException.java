package de.hybris.platform.patches.exceptions;

public class PatchImportException extends RuntimeException
{
    public PatchImportException(Throwable cause)
    {
        super(cause);
    }


    public PatchImportException(String message)
    {
        super(message);
    }


    public PatchImportException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
