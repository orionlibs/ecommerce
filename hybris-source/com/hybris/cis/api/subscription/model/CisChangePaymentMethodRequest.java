package com.hybris.cis.api.subscription.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionChangePaymentMethodRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisChangePaymentMethodRequest extends CisSubscriptionRequest
{
    @XmlElement(name = "merchantPaymentMethodId", required = true)
    private String merchantPaymentMethodId;
    @XmlElement(name = "action", required = true)
    private String action;
    @XmlElement(name = "newValue")
    private String newValue;
    @XmlElement(name = "propagate")
    private boolean propagate;


    public String getMerchantPaymentMethodId()
    {
        return this.merchantPaymentMethodId;
    }


    public void setMerchantPaymentMethodId(String merchantPaymentMethodId)
    {
        this.merchantPaymentMethodId = merchantPaymentMethodId;
    }


    public String getAction()
    {
        return this.action;
    }


    public void setAction(String action)
    {
        this.action = action;
    }


    public String getNewValue()
    {
        return this.newValue;
    }


    public void setNewValue(String newValue)
    {
        this.newValue = newValue;
    }


    public boolean isPropagate()
    {
        return this.propagate;
    }


    public void setPropagate(boolean propagate)
    {
        this.propagate = propagate;
    }
}
