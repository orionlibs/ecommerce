package com.hybris.cis.api.subscription.mock.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CustomerUsageMock implements Serializable
{
    private static final long serialVersionUID = -2687557503081970984L;
    private Date fromDate;
    private Date toDate;
    private String customerId;
    private String subscriptionId;
    private String usageChargeName;
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
