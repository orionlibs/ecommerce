package de.hybris.platform.acceleratorfacades.urlencoder.data;

import java.io.Serializable;

public class UrlEncoderData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String attributeName;
    private String currentValue;
    private String defaultValue;


    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }


    public String getAttributeName()
    {
        return this.attributeName;
    }


    public void setCurrentValue(String currentValue)
    {
        this.currentValue = currentValue;
    }


    public String getCurrentValue()
    {
        return this.currentValue;
    }


    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    public String getDefaultValue()
    {
        return this.defaultValue;
    }
}
