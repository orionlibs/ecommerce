package de.hybris.platform.commercewebservicescommons.dto.storesession;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Currency", description = "Representation of a Currency")
public class CurrencyWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "isocode", value = "Code of the currency in iso format")
    private String isocode;
    @ApiModelProperty(name = "name", value = "Name of the currency")
    private String name;
    @ApiModelProperty(name = "active", value = "Boolean flag whether currency is active")
    private Boolean active;
    @ApiModelProperty(name = "symbol", value = "Symbol of the currency")
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


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
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
