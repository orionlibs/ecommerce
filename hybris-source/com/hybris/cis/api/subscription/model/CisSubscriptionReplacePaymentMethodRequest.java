package com.hybris.cis.api.subscription.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionReplacePaymentMethodRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionReplacePaymentMethodRequest extends CisSubscriptionRequest
{
    @XmlElement(name = "merchantSubscriptionId")
    private String merchantSubscriptionId;
    @XmlElement(name = "merchantPaymentMethodId")
    private String merchantPaymentMethodId;


    public String getMerchantSubscriptionId()
    {
        return this.merchantSubscriptionId;
    }


    public void setMerchantSubscriptionId(String merchantSubscriptionId)
    {
        this.merchantSubscriptionId = merchantSubscriptionId;
    }


    public String getMerchantPaymentMethodId()
    {
        return this.merchantPaymentMethodId;
    }


    public void setMerchantPaymentMethodId(String merchantPaymentMethodId)
    {
        this.merchantPaymentMethodId = merchantPaymentMethodId;
    }
}
