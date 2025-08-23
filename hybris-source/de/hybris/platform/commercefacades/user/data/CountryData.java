package de.hybris.platform.commercefacades.user.data;

import java.io.Serializable;

public class CountryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String isocode;
    private String name;


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
}
