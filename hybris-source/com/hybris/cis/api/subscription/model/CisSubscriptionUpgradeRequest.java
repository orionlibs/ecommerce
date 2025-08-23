package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.validation.XSSSafe;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionUpgradeRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionUpgradeRequest extends CisSubscriptionCreateRequest
{
    @XmlElement(name = "sourceSubscriptionId")
    @XSSSafe
    private String merchantSourceSubscriptionId;
    @XmlElement(name = "settlement")
    @XSSSafe
    private String settlement;
    @XmlElement(name = "effectiveFrom")
    @XSSSafe
    private String effectiveFrom;
    @XmlElement(name = "preview")
    private boolean preview;


    public String getEffectiveFrom()
    {
        return this.effectiveFrom;
    }


    public void setEffectiveFrom(String effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    public String getMerchantSourceSubscriptionId()
    {
        return this.merchantSourceSubscriptionId;
    }


    public void setMerchantSourceSubscriptionId(String merchantSourceSubscriptionId)
    {
        this.merchantSourceSubscriptionId = merchantSourceSubscriptionId;
    }


    public String getSettlement()
    {
        return this.settlement;
    }


    public void setSettlement(String settlement)
    {
        this.settlement = settlement;
    }


    public boolean isPreview()
    {
        return this.preview;
    }


    public void setPreview(boolean preview)
    {
        this.preview = preview;
    }
}
