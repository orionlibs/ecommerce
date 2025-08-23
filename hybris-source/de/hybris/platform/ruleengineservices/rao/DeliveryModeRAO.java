package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class DeliveryModeRAO implements Serializable
{
    private String code;
    private BigDecimal cost;
    private String currencyIsoCode;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCost(BigDecimal cost)
    {
        this.cost = cost;
    }


    public BigDecimal getCost()
    {
        return this.cost;
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
        DeliveryModeRAO other = (DeliveryModeRAO)o;
        return Objects.equals(getCode(), other.getCode());
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.code;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
