package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "Price", description = "Representation of a Price")
public class PriceWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "currencyIso", value = "Currency iso format")
    private String currencyIso;
    @ApiModelProperty(name = "value", value = "Value of price in BigDecimal format")
    private BigDecimal value;
    @ApiModelProperty(name = "priceType", value = "Type of the price")
    private PriceWsDTOType priceType;
    @ApiModelProperty(name = "formattedValue", value = "Value of price formatted")
    private String formattedValue;
    @ApiModelProperty(name = "minQuantity", value = "Minimum quantity of the price value")
    private Long minQuantity;
    @ApiModelProperty(name = "maxQuantity", value = "Maximum quantity of the price value")
    private Long maxQuantity;


    public void setCurrencyIso(String currencyIso)
    {
        this.currencyIso = currencyIso;
    }


    public String getCurrencyIso()
    {
        return this.currencyIso;
    }


    public void setValue(BigDecimal value)
    {
        this.value = value;
    }


    public BigDecimal getValue()
    {
        return this.value;
    }


    public void setPriceType(PriceWsDTOType priceType)
    {
        this.priceType = priceType;
    }


    public PriceWsDTOType getPriceType()
    {
        return this.priceType;
    }


    public void setFormattedValue(String formattedValue)
    {
        this.formattedValue = formattedValue;
    }


    public String getFormattedValue()
    {
        return this.formattedValue;
    }


    public void setMinQuantity(Long minQuantity)
    {
        this.minQuantity = minQuantity;
    }


    public Long getMinQuantity()
    {
        return this.minQuantity;
    }


    public void setMaxQuantity(Long maxQuantity)
    {
        this.maxQuantity = maxQuantity;
    }


    public Long getMaxQuantity()
    {
        return this.maxQuantity;
    }
}
