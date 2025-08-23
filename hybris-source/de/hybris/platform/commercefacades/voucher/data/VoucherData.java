package de.hybris.platform.commercefacades.voucher.data;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import java.io.Serializable;

public class VoucherData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String voucherCode;
    private String name;
    private String description;
    private Double value;
    private String valueFormatted;
    private String valueString;
    private boolean freeShipping;
    private CurrencyData currency;
    private PriceData appliedValue;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setVoucherCode(String voucherCode)
    {
        this.voucherCode = voucherCode;
    }


    public String getVoucherCode()
    {
        return this.voucherCode;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setValue(Double value)
    {
        this.value = value;
    }


    public Double getValue()
    {
        return this.value;
    }


    public void setValueFormatted(String valueFormatted)
    {
        this.valueFormatted = valueFormatted;
    }


    public String getValueFormatted()
    {
        return this.valueFormatted;
    }


    public void setValueString(String valueString)
    {
        this.valueString = valueString;
    }


    public String getValueString()
    {
        return this.valueString;
    }


    public void setFreeShipping(boolean freeShipping)
    {
        this.freeShipping = freeShipping;
    }


    public boolean isFreeShipping()
    {
        return this.freeShipping;
    }


    public void setCurrency(CurrencyData currency)
    {
        this.currency = currency;
    }


    public CurrencyData getCurrency()
    {
        return this.currency;
    }


    public void setAppliedValue(PriceData appliedValue)
    {
        this.appliedValue = appliedValue;
    }


    public PriceData getAppliedValue()
    {
        return this.appliedValue;
    }
}
