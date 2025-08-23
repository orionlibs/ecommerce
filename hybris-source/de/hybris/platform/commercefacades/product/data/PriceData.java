package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class PriceData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String currencyIso;
    private BigDecimal value;
    private PriceDataType priceType;
    private String formattedValue;
    private Long minQuantity;
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


    public void setPriceType(PriceDataType priceType)
    {
        this.priceType = priceType;
    }


    public PriceDataType getPriceType()
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
