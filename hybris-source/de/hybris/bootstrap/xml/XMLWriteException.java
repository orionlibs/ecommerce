package de.hybris.bootstrap.xml;

public class XMLWriteException extends RuntimeException
{
    private final XMLTagWriter tagWriter;


    public XMLWriteException(String message)
    {
        this(null, null, message);
    }


    public XMLWriteException(XMLTagWriter tagWriter, String message)
    {
        this(null, tagWriter, message);
    }


    public XMLWriteException(Throwable cause, XMLTagWriter tagWriter, String message)
    {
        super(message, cause);
        this.tagWriter = tagWriter;
    }


    public XMLTagWriter getTagWriter()
    {
        return this.tagWriter;
    }
}
