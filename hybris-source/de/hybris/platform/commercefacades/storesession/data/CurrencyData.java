package de.hybris.platform.commercefacades.storesession.data;

import java.io.Serializable;

public class CurrencyData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String isocode;
    private String name;
    private boolean active;
    private String symbol;


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


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public boolean isActive()
    {
        return this.active;
    }


    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }


    public String getSymbol()
    {
        return this.symbol;
    }
}
