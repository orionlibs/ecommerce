package de.hybris.platform.servicelayer.user.exceptions;

import de.hybris.platform.servicelayer.exceptions.SystemException;

public class CannotDecodePasswordException extends SystemException
{
    public CannotDecodePasswordException(String message)
    {
        super(message);
    }


    public CannotDecodePasswordException(Throwable cause)
    {
        super(cause);
    }


    public CannotDecodePasswordException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
