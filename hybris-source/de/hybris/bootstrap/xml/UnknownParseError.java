package de.hybris.bootstrap.xml;

public class UnknownParseError extends ParseAbortException
{
    public UnknownParseError(Exception nested, String msg)
    {
        super(msg, nested);
    }
}
