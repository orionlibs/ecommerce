package de.hybris.platform.acceleratorservices.storefront.data;

import java.io.Serializable;

public class JavaScriptVariableData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String qualifier;
    private String value;


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }
}
