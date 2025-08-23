package de.hybris.platform.commercefacades.user.data;

import java.io.Serializable;

public class RegionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String isocode;
    private String isocodeShort;
    private String countryIso;
    private String name;


    public void setIsocode(String isocode)
    {
        this.isocode = isocode;
    }


    public String getIsocode()
    {
        return this.isocode;
    }


    public void setIsocodeShort(String isocodeShort)
    {
        this.isocodeShort = isocodeShort;
    }


    public String getIsocodeShort()
    {
        return this.isocodeShort;
    }


    public void setCountryIso(String countryIso)
    {
        this.countryIso = countryIso;
    }


    public String getCountryIso()
    {
        return this.countryIso;
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
