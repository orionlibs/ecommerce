package com.hybris.cis.api.subscription.mock.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class UsageChargeTierMock implements Serializable
{
    private static final long serialVersionUID = -5129699093279062932L;
    private BigDecimal chargePrice = BigDecimal.ZERO;
    private Integer numberOfUnits;


    public BigDecimal getChargePrice()
    {
        return this.chargePrice;
    }


    public void setChargePrice(BigDecimal chargePrice)
    {
        this.chargePrice = chargePrice;
    }


    public Integer getNumberOfUnits()
    {
        return this.numberOfUnits;
    }


    public void setNumberOfUnits(Integer numberOfUnits)
    {
        this.numberOfUnits = numberOfUnits;
    }
}
