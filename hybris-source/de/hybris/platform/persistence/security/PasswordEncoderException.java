package de.hybris.platform.persistence.security;

public class PasswordEncoderException extends RuntimeException
{
    public PasswordEncoderException()
    {
        super("Something went wrong when encoding password.");
    }


    public PasswordEncoderException(String message)
    {
        super(message);
    }
}
