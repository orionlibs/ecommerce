package com.hybris.cis.api.subscription.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionCancelSubscriptionRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionCancelSubscriptionRequest extends CisSubscriptionRequest
{
    @XmlElement(name = "merchantSubscriptionId", required = true)
    private String merchantSubscriptionId;
    @XmlElement(name = "cancelationMode")
    private String cancelationMode;
    @XmlElement(name = "force")
    private boolean force;


    public String getMerchantSubscriptionId()
    {
        return this.merchantSubscriptionId;
    }


    public void setMerchantSubscriptionId(String merchantSubscriptionId)
    {
        this.merchantSubscriptionId = merchantSubscriptionId;
    }


    public String getCancelationMode()
    {
        return this.cancelationMode;
    }


    public void setCancelationMode(String cancelationMode)
    {
        this.cancelationMode = cancelationMode;
    }


    public boolean isForce()
    {
        return this.force;
    }


    public void setForce(boolean force)
    {
        this.force = force;
    }
}
