package de.hybris.platform.servicelayer.security.auth;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class AuthenticationException extends BusinessException
{
    public AuthenticationException(String message)
    {
        super(message);
    }


    public AuthenticationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
