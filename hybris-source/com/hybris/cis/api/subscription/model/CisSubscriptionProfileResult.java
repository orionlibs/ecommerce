package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.CisAddress;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionProfileResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionProfileResult extends CisSubscriptionTransactionResult
{
    @XmlElement(name = "profileId")
    private String profileId;
    @XmlElement(name = "currency")
    private String currency;
    @XmlElement(name = "validationResult")
    private CisSubscriptionTransactionResult validationResult;
    @XmlElementWrapper(name = "paymentMethods")
    @XmlElement(name = "paymentMethod")
    private List<CisPaymentMethod> paymentMethods;
    @XmlElementWrapper(name = "subscriptions")
    @XmlElement(name = "subscriptions")
    private List<CisSubscriptionData> subscriptions;
    @XmlElement(name = "customerAddress")
    private CisAddress customerAddress;
    @XmlElement(name = "comments")
    private String comments;


    public String getProfileId()
    {
        return this.profileId;
    }


    public void setProfileId(String profileId)
    {
        this.profileId = profileId;
    }


    public CisSubscriptionTransactionResult getValidationResult()
    {
        return this.validationResult;
    }


    public void setValidationResult(CisSubscriptionTransactionResult validationResult)
    {
        this.validationResult = validationResult;
    }


    public String getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(String currency)
    {
        this.currency = currency;
    }


    public List<CisPaymentMethod> getPaymentMethods()
    {
        return this.paymentMethods;
    }


    public void setPaymentMethods(List<CisPaymentMethod> paymentMethods)
    {
        this.paymentMethods = paymentMethods;
    }


    public CisAddress getCustomerAddress()
    {
        return this.customerAddress;
    }


    public void setCustomerAddress(CisAddress customerAddress)
    {
        this.customerAddress = customerAddress;
    }


    public String getComments()
    {
        return this.comments;
    }


    public void setComments(String comments)
    {
        this.comments = comments;
    }


    public List<CisSubscriptionData> getSubscriptions()
    {
        return this.subscriptions;
    }


    public void setSubscriptions(List<CisSubscriptionData> subscriptions)
    {
        this.subscriptions = subscriptions;
    }
}
