package com.hybris.cis.api.subscription.mock.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class ChargeMock implements Serializable
{
    private static final long serialVersionUID = -3738716961071562180L;
    private BigDecimal chargePrice = BigDecimal.ZERO;
    private Integer numberOfCycles;
    private String oneTimeChargeTime;
    private Map<String, String> vendorParameters;


    public BigDecimal getChargePrice()
    {
        return this.chargePrice;
    }


    public void setChargePrice(BigDecimal chargePrice)
    {
        this.chargePrice = chargePrice;
    }


    public Integer getNumberOfCycles()
    {
        return this.numberOfCycles;
    }


    public void setNumberOfCycles(Integer numberOfCycles)
    {
        this.numberOfCycles = numberOfCycles;
    }


    public String getOneTimeChargeTime()
    {
        return this.oneTimeChargeTime;
    }


    public void setOneTimeChargeTime(String oneTimeChargeTime)
    {
        this.oneTimeChargeTime = oneTimeChargeTime;
    }


    public Map<String, String> getVendorParameters()
    {
        return this.vendorParameters;
    }


    public void setVendorParameters(Map<String, String> vendorParameters)
    {
        this.vendorParameters = vendorParameters;
    }
}
