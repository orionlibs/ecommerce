package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.AnnotationHashMap;
import com.hybris.cis.api.validation.XSSSafe;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CisTermsOfService
{
    @XmlAttribute(name = "id")
    @XSSSafe
    private String id;
    @XmlElement(name = "name")
    @XSSSafe
    private String name;
    @XmlElement(name = "number")
    private int number;
    @XmlElement(name = "frequency")
    @XSSSafe
    private String frequency;
    @XmlElement(name = "autoRenewal")
    private Boolean autoRenewal;
    @XmlElement(name = "cancellable")
    private Boolean cancellable;
    @XmlElement(name = "billingPlanId")
    @XSSSafe
    private String billingPlanId;
    @XmlElement(name = "billingPlanName")
    @XSSSafe
    private String billingPlanName;
    @XmlElement(name = "billingCycleDay")
    private int billingCycleDay;
    @XmlElement(name = "billingFrequency")
    @XSSSafe
    private String billingFrequency;
    @XmlElement(name = "vendorParameters")
    @Valid
    private AnnotationHashMap vendorParameters;


    public String getId()
    {
        return this.id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public int getNumber()
    {
        return this.number;
    }


    public void setNumber(int number)
    {
        this.number = number;
    }


    public String getFrequency()
    {
        return this.frequency;
    }


    public void setFrequency(String frequency)
    {
        this.frequency = frequency;
    }


    public Boolean getAutoRenewal()
    {
        return this.autoRenewal;
    }


    public void setAutoRenewal(Boolean autoRenewal)
    {
        this.autoRenewal = autoRenewal;
    }


    public Boolean getCancellable()
    {
        return this.cancellable;
    }


    public void setCancellable(Boolean cancellable)
    {
        this.cancellable = cancellable;
    }


    public String getBillingPlanId()
    {
        return this.billingPlanId;
    }


    public void setBillingPlanId(String billingPlanId)
    {
        this.billingPlanId = billingPlanId;
    }


    public String getBillingPlanName()
    {
        return this.billingPlanName;
    }


    public void setBillingPlanName(String billingPlanName)
    {
        this.billingPlanName = billingPlanName;
    }


    public int getBillingCycleDay()
    {
        return this.billingCycleDay;
    }


    public void setBillingCycleDay(int billingCycleDay)
    {
        this.billingCycleDay = billingCycleDay;
    }


    public String getBillingFrequency()
    {
        return this.billingFrequency;
    }


    public void setBillingFrequency(String billingFrequency)
    {
        this.billingFrequency = billingFrequency;
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
