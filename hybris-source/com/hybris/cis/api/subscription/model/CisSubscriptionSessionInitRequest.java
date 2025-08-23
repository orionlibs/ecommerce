package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.validation.XSSSafe;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionSessionInitRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionSessionInitRequest extends CisSubscriptionRequest
{
    @XmlElement(name = "returnUrl")
    @XSSSafe
    private String returnUrl;
    @XmlElement(name = "cancelReturnUrl")
    @XSSSafe
    private String cancelReturnUrl;
    @XmlElement(name = "ipAddress")
    @XSSSafe
    private String ipAddress;
    @XmlElement(name = "merchantAccountId")
    @XSSSafe
    private String merchantAccountId;


    public String getReturnUrl()
    {
        return this.returnUrl;
    }


    public void setReturnUrl(String returnUrl)
    {
        this.returnUrl = returnUrl;
    }


    public String getCancelReturnUrl()
    {
        return this.cancelReturnUrl;
    }


    public void setCancelReturnUrl(String cancelReturnUrl)
    {
        this.cancelReturnUrl = cancelReturnUrl;
    }


    public String toString()
    {
        return "CisSubscriptionSessionInitRequest [ipAddress=" + this.ipAddress + "returnUrl=" + this.returnUrl + ", cancelReturnUrl=" + this.cancelReturnUrl + ",  merchantAccountId=" + this.merchantAccountId + "]";
    }


    public String getMerchantAccountId()
    {
        return this.merchantAccountId;
    }


    public void setMerchantAccountId(String merchantAccountId)
    {
        this.merchantAccountId = merchantAccountId;
    }


    public String getIpAddress()
    {
        return this.ipAddress;
    }


    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }
}
