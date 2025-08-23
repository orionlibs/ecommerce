package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.validation.XSSSafe;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionProfileRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionProfileRequest extends CisSubscriptionRequest
{
    @XmlElement(name = "profileId", required = true)
    @XSSSafe
    private String profileId;
    @XmlElement(name = "customerName")
    @XSSSafe
    private String customerName;
    @XmlElement(name = "company")
    @XSSSafe
    private String company;
    @XmlElement(name = "currency")
    @XSSSafe
    private String currency;
    @XmlElement(name = "emailPreference")
    @XSSSafe
    private String emailPreference;
    @XmlElement(name = "languagePreference", required = true)
    @XSSSafe
    private String languagePreference;
    @XmlElement(name = "emailAddress", required = true)
    @XSSSafe
    private String emailAddress;
    @XmlElement(name = "shippingAddress")
    @Valid
    private CisAddress shippingAddress;


    public String getCustomerName()
    {
        return this.customerName;
    }


    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }


    public String getCompany()
    {
        return this.company;
    }


    public void setCompany(String company)
    {
        this.company = company;
    }


    public String getEmailPreference()
    {
        return this.emailPreference;
    }


    public void setEmailPreference(String emailPreference)
    {
        this.emailPreference = emailPreference;
    }


    public String getLanguagePreference()
    {
        return this.languagePreference;
    }


    public void setLanguagePreference(String languagePreference)
    {
        this.languagePreference = languagePreference;
    }


    public String getProfileId()
    {
        return this.profileId;
    }


    public void setProfileId(String profileId)
    {
        this.profileId = profileId;
    }


    public CisAddress getShippingAddress()
    {
        return this.shippingAddress;
    }


    public void setShippingAddress(CisAddress shippingAddress)
    {
        this.shippingAddress = shippingAddress;
    }


    public String getEmailAddress()
    {
        return this.emailAddress;
    }


    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }


    public String getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(String currency)
    {
        this.currency = currency;
    }
}
