package com.hybris.cis.api.subscription.mock.data;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "billing")
@XmlAccessorType(XmlAccessType.FIELD)
public class BillingInfoMock
{
    @XmlElement(name = "billingId")
    private String billingId;
    @XmlElement(name = "billingMonthOffset")
    private Integer billingMonthOffset;
    @XmlElement(name = "billingPeriod")
    private String billingPeriod;
    @XmlElement(name = "billingDate")
    @XmlSchemaType(name = "date")
    private Date billingDate;
    @XmlElement(name = "amount")
    private String amount;
    @XmlElement(name = "status")
    private String status;


    public String getBillingId()
    {
        return this.billingId;
    }


    public void setBillingId(String billingId)
    {
        this.billingId = billingId;
    }


    public Integer getBillingMonthOffset()
    {
        return this.billingMonthOffset;
    }


    public void setBillingMonthOffset(Integer billingMonthOffset)
    {
        this.billingMonthOffset = billingMonthOffset;
    }


    public String getBillingPeriod()
    {
        return this.billingPeriod;
    }


    public void setBillingPeriod(String billingPeriod)
    {
        this.billingPeriod = billingPeriod;
    }


    public Date getBillingDate()
    {
        return (this.billingDate == null) ? null : new Date(this.billingDate.getTime());
    }


    public void setBillingDate(Date billingDate)
    {
        this.billingDate = (billingDate == null) ? null : new Date(billingDate.getTime());
    }


    public String getAmount()
    {
        return this.amount;
    }


    public void setAmount(String amount)
    {
        this.amount = amount;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }
}
