package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class DiscountValueRAO implements Serializable
{
    private BigDecimal value;
    private String currencyIsoCode;


    public void setValue(BigDecimal value)
    {
        this.value = value;
    }


    public BigDecimal getValue()
    {
        return this.value;
    }


    public void setCurrencyIsoCode(String currencyIsoCode)
    {
        this.currencyIsoCode = currencyIsoCode;
    }


    public String getCurrencyIsoCode()
    {
        return this.currencyIsoCode;
    }


    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        if(o == this)
        {
            return true;
        }
        if(getClass() != o.getClass())
        {
            return false;
        }
        DiscountValueRAO other = (DiscountValueRAO)o;
        return (Objects.equals(getValue(), other.getValue()) &&
                        Objects.equals(getCurrencyIsoCode(), other.getCurrencyIsoCode()));
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.value;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.currencyIsoCode;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
