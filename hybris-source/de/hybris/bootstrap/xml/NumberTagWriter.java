package de.hybris.bootstrap.xml;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.znerd.xmlenc.XMLOutputter;

public class NumberTagWriter extends SimpleTagWriter
{
    private String numberFormatDef = null;
    private NumberFormat numberFormat = null;


    public NumberTagWriter(XMLTagWriter parent, String tagName)
    {
        this(parent, tagName, false);
    }


    public NumberTagWriter(XMLTagWriter parent, String tagName, boolean mandatory)
    {
        super(parent, tagName, mandatory);
    }


    public void setFormat(String pattern) throws IllegalStateException
    {
        if(this.numberFormat != null)
        {
            throw new IllegalStateException("already got a number format for output");
        }
        this.numberFormatDef = pattern;
    }


    protected NumberFormat createNumberFormat()
    {
        return (this.numberFormatDef != null) ? new DecimalFormat(this.numberFormatDef) : NumberFormat.getNumberInstance();
    }


    protected final NumberFormat getNumberFormat()
    {
        if(this.numberFormat == null)
        {
            this.numberFormat = createNumberFormat();
        }
        return this.numberFormat;
    }


    protected void writeContent(XMLOutputter xmlOut, Object object) throws IOException
    {
        Object localObject = object;
        if(localObject instanceof Number)
        {
            if(localObject instanceof Double)
            {
                localObject = getNumberFormat().format(((Double)localObject).doubleValue());
            }
            else
            {
                localObject = getNumberFormat().format(((Number)localObject).longValue());
            }
        }
        super.writeContent(xmlOut, localObject);
    }
}
