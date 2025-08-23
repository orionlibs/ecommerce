package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.AnnotationHashMap;
import com.hybris.cis.api.validation.XSSSafe;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionItem
{
    @XmlAttribute(name = "id")
    @XSSSafe
    private String id;
    @XmlElement(name = "code")
    @XSSSafe
    private String code;
    @XmlElement(name = "name")
    @XSSSafe
    private String name;
    @XmlElement(name = "subscriptionTerm")
    @Valid
    private CisTermsOfService subscriptionTerm;
    @XmlElement(name = "subscriptionPlan")
    @Valid
    private CisSubscriptionPlan subscriptionPlan;
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


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public CisSubscriptionPlan getSubscriptionPlan()
    {
        return this.subscriptionPlan;
    }


    public void setSubscriptionPlan(CisSubscriptionPlan subscriptionPlan)
    {
        this.subscriptionPlan = subscriptionPlan;
    }


    public CisTermsOfService getSubscriptionTerm()
    {
        return this.subscriptionTerm;
    }


    public void setSubscriptionTerm(CisTermsOfService subscriptionTerm)
    {
        this.subscriptionTerm = subscriptionTerm;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
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
