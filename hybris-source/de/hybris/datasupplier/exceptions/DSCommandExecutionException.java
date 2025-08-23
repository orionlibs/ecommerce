package de.hybris.datasupplier.exceptions;

public class DSCommandExecutionException extends Exception
{
    private static final long serialVersionUID = 1L;


    public DSCommandExecutionException()
    {
    }


    public DSCommandExecutionException(String cause)
    {
        super(cause);
    }


    public DSCommandExecutionException(Throwable ex)
    {
        super(ex);
    }


    public DSCommandExecutionException(String cause, Throwable ex)
    {
        super(cause, ex);
    }
}
