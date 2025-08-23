package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.AnnotationHashMap;
import com.hybris.cis.api.validation.XSSSafe;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CisChargeEntry
{
    @XmlElement(name = "chargePrice", required = true)
    private BigDecimal chargePrice = BigDecimal.ZERO;
    @XmlElement(name = "numberOfCycles")
    private Integer numberOfCycles;
    @XmlElement(name = "oneTimeChargeTime")
    @XSSSafe
    private String oneTimeChargeTime;
    @XmlElement(name = "vendorParameters")
    @Valid
    private AnnotationHashMap vendorParameters;


    public BigDecimal getChargePrice()
    {
        return this.chargePrice;
    }


    public void setChargePrice(BigDecimal chargePrice)
    {
        this.chargePrice = chargePrice;
    }


    public String getOneTimeChargeTime()
    {
        return this.oneTimeChargeTime;
    }


    public void setOneTimeChargeTime(String oneTimeChargeTime)
    {
        this.oneTimeChargeTime = oneTimeChargeTime;
    }


    public Integer getNumberOfCycles()
    {
        return this.numberOfCycles;
    }


    public void setNumberOfCycles(Integer numberOfCycles)
    {
        this.numberOfCycles = numberOfCycles;
    }


    public AnnotationHashMap getVendorParameters()
    {
        return this.vendorParameters;
    }


    public void setVendorParameters(AnnotationHashMap vendorParameters)
    {
        this.vendorParameters = vendorParameters;
    }
}
