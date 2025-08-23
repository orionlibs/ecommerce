package de.hybris.platform.platformbackoffice.taxdiscountvalueparser;

public class ParserException extends Exception
{
    public ParserException(String message)
    {
        super(message);
    }


    public ParserException(Exception exception)
    {
        super(exception);
    }


    public ParserException(String message, Exception exception)
    {
        super(message, exception);
    }
}
