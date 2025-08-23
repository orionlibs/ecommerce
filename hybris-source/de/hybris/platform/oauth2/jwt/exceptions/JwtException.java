package de.hybris.platform.oauth2.jwt.exceptions;

public class JwtException extends Exception
{
    public JwtException(String msg)
    {
        super(msg);
    }


    public JwtException(String msg, Exception nested)
    {
        super(msg, nested);
    }
}
