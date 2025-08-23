package de.hybris.bootstrap.xml;

import org.xml.sax.SAXException;

public abstract class ParseAbortException extends SAXException
{
    public ParseAbortException(String msg)
    {
        super(msg);
    }


    public ParseAbortException(String msg, Exception nested)
    {
        super(msg, nested);
    }
}
