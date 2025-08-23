package de.hybris.bootstrap.xml;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.znerd.xmlenc.XMLOutputter;

public class DateTagWriter extends SimpleTagWriter
{
    private String dateFormatPattern = null;
    private DateFormat dateFormat = null;


    public DateTagWriter(XMLTagWriter parent, String tagName)
    {
        super(parent, tagName, false);
    }


    public DateTagWriter(XMLTagWriter parent, String tagName, boolean mandatory)
    {
        super(parent, tagName, mandatory);
    }


    public void setDateFormat(String pattern) throws IllegalStateException
    {
        if(this.dateFormat != null)
        {
            throw new IllegalStateException("already got a date format");
        }
        this.dateFormatPattern = pattern;
    }


    protected DateFormat createDateFormat()
    {
        return (this.dateFormatPattern != null) ? new SimpleDateFormat(this.dateFormatPattern) : DateFormat.getDateTimeInstance();
    }


    protected final DateFormat getDateFormat()
    {
        return (this.dateFormat != null) ? this.dateFormat : (this.dateFormat = createDateFormat());
    }


    protected void writeContent(XMLOutputter xmlOut, Object object) throws IOException
    {
        Object localObject = object;
        if(localObject instanceof Date)
        {
            localObject = getDateFormat().format((Date)localObject);
        }
        super.writeContent(xmlOut, localObject);
    }
}
