package de.hybris.platform.acceleratorservices.storefront.data;

import java.io.Serializable;

public class MetaElementData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private String content;
    private String property;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setContent(String content)
    {
        this.content = content;
    }


    public String getContent()
    {
        return this.content;
    }


    public void setProperty(String property)
    {
        this.property = property;
    }


    public String getProperty()
    {
        return this.property;
    }
}
