package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.validation.XSSSafe;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "paymentMethodUpdateRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisPaymentMethodUpdateRequest extends CisSubscriptionRequest
{
    @XmlElement(name = "merchantPaymentMethodId", required = true)
    @XSSSafe
    private String merchantPaymentMethodId;
    @XmlElement(name = "enabled")
    private Boolean enabled;
    @XmlElement(name = "billingAddress")
    @Valid
    private CisAddress billingAddress;
    @XmlElement(name = "propagate")
    private Boolean propagate;


    public String getMerchantPaymentMethodId()
    {
        return this.merchantPaymentMethodId;
    }


    public void setMerchantPaymentMethodId(String merchantPaymentMethodId)
    {
        this.merchantPaymentMethodId = merchantPaymentMethodId;
    }


    public Boolean getEnabled()
    {
        return this.enabled;
    }


    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }


    public CisAddress getBillingAddress()
    {
        return this.billingAddress;
    }


    public void setBillingAddress(CisAddress billingAddress)
    {
        this.billingAddress = billingAddress;
    }


    public Boolean getPropagate()
    {
        return this.propagate;
    }


    public void setPropagate(Boolean propagate)
    {
        this.propagate = propagate;
    }
}
