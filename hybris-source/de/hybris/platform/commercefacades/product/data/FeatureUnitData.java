package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;

public class FeatureUnitData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String symbol;
    private String name;
    private String unitType;


    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }


    public String getSymbol()
    {
        return this.symbol;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setUnitType(String unitType)
    {
        this.unitType = unitType;
    }


    public String getUnitType()
    {
        return this.unitType;
    }
}
