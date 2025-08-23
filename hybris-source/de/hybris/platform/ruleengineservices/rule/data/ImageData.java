package de.hybris.platform.ruleengineservices.rule.data;

import java.io.Serializable;

public class ImageData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String url;
    private String altText;
    private String format;


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setAltText(String altText)
    {
        this.altText = altText;
    }


    public String getAltText()
    {
        return this.altText;
    }


    public void setFormat(String format)
    {
        this.format = format;
    }


    public String getFormat()
    {
        return this.format;
    }
}
