package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.validation.XSSSafe;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionUpdateRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionUpdateRequest extends AbstractCisSubscriptionUpdateRequest
{
    @XmlElement(name = "merchantPaymentMethodId")
    @XSSSafe
    private String merchantPaymentMethodId;
    @XmlElement(name = "autoRenewal")
    private Boolean autoRenewal;
    @XmlElement(name = "contractDurationExtension")
    private Integer contractDurationExtension;


    public Integer getContractDurationExtension()
    {
        return this.contractDurationExtension;
    }


    public void setContractDurationExtension(Integer contractDurationExtension)
    {
        this.contractDurationExtension = contractDurationExtension;
    }


    public Boolean getAutoRenewal()
    {
        return this.autoRenewal;
    }


    public void setAutoRenewal(Boolean autoRenewal)
    {
        this.autoRenewal = autoRenewal;
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
