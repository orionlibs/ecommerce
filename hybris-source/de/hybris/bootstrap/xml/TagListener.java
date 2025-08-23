package de.hybris.bootstrap.xml;

import org.xml.sax.Attributes;

public interface TagListener
{
    void characters(char[] paramArrayOfchar, int paramInt1, int paramInt2);


    void startElement(ObjectProcessor paramObjectProcessor, Attributes paramAttributes) throws ParseAbortException;


    void endElement(ObjectProcessor paramObjectProcessor) throws ParseAbortException;


    void setStartLineNumber(int paramInt);


    void setEndLineNumber(int paramInt);


    TagListener getSubTagListener(String paramString);


    String getTagName();


    String getAttribute(String paramString);


    Object getResult();
}
