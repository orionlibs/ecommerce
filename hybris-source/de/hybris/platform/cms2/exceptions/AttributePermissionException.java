package de.hybris.platform.cms2.exceptions;

public class AttributePermissionException extends RuntimeException
{
    public AttributePermissionException(String message)
    {
        super(message);
    }


    public AttributePermissionException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public AttributePermissionException(Throwable cause)
    {
        super(cause);
    }
}
