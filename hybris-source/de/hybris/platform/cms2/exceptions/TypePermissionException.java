package de.hybris.platform.cms2.exceptions;

public class TypePermissionException extends RuntimeException
{
    private static final long serialVersionUID = 1540721153938113913L;


    public TypePermissionException(String message)
    {
        super(message);
    }


    public TypePermissionException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public TypePermissionException(Throwable cause)
    {
        super(cause);
    }
}
