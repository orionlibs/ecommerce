package de.hybris.platform.ruleengineservices.rao;

import java.math.BigDecimal;

public class DiscountRAO extends AbstractRuleActionRAO
{
    private BigDecimal value;
    private String currencyIsoCode;
    private long appliedToQuantity;
    private boolean perUnit;


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


    public void setAppliedToQuantity(long appliedToQuantity)
    {
        this.appliedToQuantity = appliedToQuantity;
    }


    public long getAppliedToQuantity()
    {
        return this.appliedToQuantity;
    }


    public void setPerUnit(boolean perUnit)
    {
        this.perUnit = perUnit;
    }


    public boolean isPerUnit()
    {
        return this.perUnit;
    }
}
