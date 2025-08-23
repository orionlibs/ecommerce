package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "FeatureUnit", description = "Representation of a Feature Unit")
public class FeatureUnitWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "symbol", value = "Symbol of the feature unit")
    private String symbol;
    @ApiModelProperty(name = "name", value = "Name of the feature unit")
    private String name;
    @ApiModelProperty(name = "unitType", value = "Type of the feature unit")
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
