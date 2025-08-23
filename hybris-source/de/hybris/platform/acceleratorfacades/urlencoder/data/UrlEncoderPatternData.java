package de.hybris.platform.acceleratorfacades.urlencoder.data;

import java.io.Serializable;

public class UrlEncoderPatternData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pattern;
    private boolean redirectRequired;


    public void setPattern(String pattern)
    {
        this.pattern = pattern;
    }


    public String getPattern()
    {
        return this.pattern;
    }


    public void setRedirectRequired(boolean redirectRequired)
    {
        this.redirectRequired = redirectRequired;
    }


    public boolean isRedirectRequired()
    {
        return this.redirectRequired;
    }
}
