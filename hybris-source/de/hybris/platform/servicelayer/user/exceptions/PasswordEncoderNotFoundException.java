package de.hybris.platform.servicelayer.user.exceptions;

import de.hybris.platform.servicelayer.exceptions.SystemException;

public class PasswordEncoderNotFoundException extends SystemException
{
    public PasswordEncoderNotFoundException(String message)
    {
        super(message);
    }


    public PasswordEncoderNotFoundException(Throwable cause)
    {
        super(cause);
    }


    public PasswordEncoderNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
