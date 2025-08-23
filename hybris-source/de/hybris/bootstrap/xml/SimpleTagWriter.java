package de.hybris.bootstrap.xml;

import java.io.IOException;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

public class SimpleTagWriter extends XMLTagWriter
{
    private final String tagName;


    public SimpleTagWriter(XMLTagWriter parent, String tagName)
    {
        this(parent, tagName, false);
    }


    public SimpleTagWriter(XMLTagWriter parent, String tagName, boolean mandatory)
    {
        super(parent, mandatory);
        this.tagName = tagName;
    }


    protected void writeContent(XMLOutputter xmlOut, Object object) throws IOException
    {
        if(object != null)
        {
            xmlOut.setLineBreak(LineBreak.NONE);
            xmlOut.pcdata(object.toString());
        }
    }


    protected String getTagName()
    {
        return this.tagName;
    }
}
