package com.hybris.cis.api.subscription.model;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CisUsageChargeTier
{
    @XmlElement(name = "chargePrice")
    private BigDecimal chargePrice = BigDecimal.ZERO;
    @XmlElement(name = "numberOfUnits")
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
