package de.hybris.datasupplier.exceptions;

public class DSSenderException extends Exception
{
    public DSSenderException(Throwable cause)
    {
        super(cause);
    }


    public DSSenderException(String cause)
    {
        super(cause);
    }


    public DSSenderException(String cause, Throwable ex)
    {
        super(cause, ex);
    }
}
