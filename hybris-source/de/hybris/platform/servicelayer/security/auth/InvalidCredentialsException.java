package de.hybris.platform.servicelayer.security.auth;

public class InvalidCredentialsException extends AuthenticationException
{
    public InvalidCredentialsException(String message)
    {
        super(message);
    }
}
