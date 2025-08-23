package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.AnnotationHashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionOrderPostRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionOrderPostRequest extends CisSubscriptionRequest
{
    @XmlElement(name = "merchantAccountId")
    private String merchantAccountId;
    @XmlElement(name = "merchantPaymentMethodId")
    private String merchantPaymentMethodId;
    @XmlElement(name = "subscriptionOrder")
    private CisSubscriptionOrder subscriptionOrder;
    @XmlElement(name = "currency")
    private String currency;
    @XmlElement(name = "vendorParameters")
    private AnnotationHashMap parameters = new AnnotationHashMap();


    public String toString()
    {
        return "CisSubscriptionOrderPostRequest [merchantAccountId=" + this.merchantAccountId + "]";
    }


    public AnnotationHashMap getParameters()
    {
        return this.parameters;
    }


    public void setParameters(AnnotationHashMap parameters)
    {
        this.parameters = parameters;
    }


    public String getMerchantAccountId()
    {
        return this.merchantAccountId;
    }


    public void setMerchantAccountId(String merchantAccountId)
    {
        this.merchantAccountId = merchantAccountId;
    }


    public CisSubscriptionOrder getSubscriptionOrder()
    {
        return this.subscriptionOrder;
    }


    public void setSubscriptionOrder(CisSubscriptionOrder subscriptionOrder)
    {
        this.subscriptionOrder = subscriptionOrder;
    }


    public String getMerchantPaymentMethodId()
    {
        return this.merchantPaymentMethodId;
    }


    public void setMerchantPaymentMethodId(String merchantPaymentMethodId)
    {
        this.merchantPaymentMethodId = merchantPaymentMethodId;
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
