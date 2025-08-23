package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.validation.XSSSafe;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customerUsageRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisCustomerUsageRequest
{
    @XmlElement(name = "fromDate")
    private Date fromDate;
    @XmlElement(name = "toDate")
    private Date toDate;
    @XmlElement(name = "customerId")
    @XSSSafe
    private String customerId;
    @XmlElement(name = "subscriptionId")
    @XSSSafe
    private String subscriptionId;
    @XmlElement(name = "usageChargeName")
    @XSSSafe
    private String usageChargeName;
    @XmlElement(name = "unitsConsumed")
    private BigDecimal unitsConsumed;


    public Date getFromDate()
    {
        return this.fromDate;
    }


    public void setFromDate(Date fromDate)
    {
        this.fromDate = fromDate;
    }


    public Date getToDate()
    {
        return this.toDate;
    }


    public void setToDate(Date toDate)
    {
        this.toDate = toDate;
    }


    public String getCustomerId()
    {
        return this.customerId;
    }


    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }


    public String getSubscriptionId()
    {
        return this.subscriptionId;
    }


    public void setSubscriptionId(String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }


    public String getUsageChargeName()
    {
        return this.usageChargeName;
    }


    public void setUsageChargeName(String usageChargeName)
    {
        this.usageChargeName = usageChargeName;
    }


    public BigDecimal getUnitsConsumed()
    {
        return this.unitsConsumed;
    }


    public void setUnitsConsumed(BigDecimal unitsConsumed)
    {
        this.unitsConsumed = unitsConsumed;
    }
}
