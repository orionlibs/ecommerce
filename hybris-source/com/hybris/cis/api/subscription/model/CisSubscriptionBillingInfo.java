package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.AnnotationHashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "billing")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionBillingInfo
{
    @XmlElement(name = "billingId")
    private String billingId;
    @XmlElement(name = "billingPeriod")
    private String billingPeriod;
    @XmlElement(name = "billingDate")
    private String billingDate;
    @XmlElement(name = "amount")
    private String amount;
    @XmlElement(name = "status")
    private String status;
    @XmlElement(name = "customFields")
    private AnnotationHashMap customFields;


    public String getBillingId()
    {
        return this.billingId;
    }


    public void setBillingId(String billingId)
    {
        this.billingId = billingId;
    }


    public String getBillingPeriod()
    {
        return this.billingPeriod;
    }


    public void setBillingPeriod(String billingPeriod)
    {
        this.billingPeriod = billingPeriod;
    }


    public String getBillingDate()
    {
        return this.billingDate;
    }


    public void setBillingDate(String billingDate)
    {
        this.billingDate = billingDate;
    }


    public String getAmount()
    {
        return this.amount;
    }


    public void setAmount(String amount)
    {
        this.amount = amount;
    }


    public AnnotationHashMap getCustomFields()
    {
        return this.customFields;
    }


    public void setCustomFields(AnnotationHashMap customFields)
    {
        this.customFields = customFields;
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
