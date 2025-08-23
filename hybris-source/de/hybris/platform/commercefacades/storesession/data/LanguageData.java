package de.hybris.platform.commercefacades.storesession.data;

import java.io.Serializable;

public class LanguageData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String isocode;
    private String name;
    private String nativeName;
    private boolean active;
    private boolean required;


    public void setIsocode(String isocode)
    {
        this.isocode = isocode;
    }


    public String getIsocode()
    {
        return this.isocode;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setNativeName(String nativeName)
    {
        this.nativeName = nativeName;
    }


    public String getNativeName()
    {
        return this.nativeName;
    }


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public boolean isActive()
    {
        return this.active;
    }


    public void setRequired(boolean required)
    {
        this.required = required;
    }


    public boolean isRequired()
    {
        return this.required;
    }
}
