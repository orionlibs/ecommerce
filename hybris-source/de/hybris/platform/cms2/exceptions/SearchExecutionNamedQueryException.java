package de.hybris.platform.cms2.exceptions;

public class SearchExecutionNamedQueryException extends RuntimeException
{
    private static final long serialVersionUID = -5513694400462128765L;


    public SearchExecutionNamedQueryException()
    {
    }


    public SearchExecutionNamedQueryException(String message)
    {
        super(message);
    }


    public SearchExecutionNamedQueryException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
