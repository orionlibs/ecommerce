package de.hybris.bootstrap.xml;

public class InvalidFormatException extends ParseAbortException
{
    public InvalidFormatException(String msg)
    {
        super(msg);
    }


    public InvalidFormatException(String msg, Exception nested)
    {
        super(msg, nested);
    }
}
