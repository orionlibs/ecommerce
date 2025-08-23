package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.validation.XSSSafe;
import javax.xml.bind.annotation.XmlElement;

public abstract class AbstractCisSubscriptionUpdateRequest extends CisSubscriptionRequest
{
    @XmlElement(name = "merchantSubscriptionId")
    @XSSSafe
    private String merchantSubscriptionId;
    @XmlElement(name = "effectiveFrom")
    @XSSSafe
    private String effectiveFrom;


    public String getMerchantSubscriptionId()
    {
        return this.merchantSubscriptionId;
    }


    public void setMerchantSubscriptionId(String merchantSubscriptionId)
    {
        this.merchantSubscriptionId = merchantSubscriptionId;
    }


    public String getEffectiveFrom()
    {
        return this.effectiveFrom;
    }


    public void setEffectiveFrom(String effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }
}
