package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.CisAddress;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CisPaymentMethod
{
    @XmlElement(name = "billingAddress")
    private CisAddress billingAddress;
    @XmlElement(name = "cardType")
    private String cardType;
    @XmlElement(name = "cardHolder")
    private String cardHolder;
    @XmlElement(name = "ccNumber")
    private String ccNumber;
    @XmlElement(name = "expirationMonth")
    private int expirationMonth;
    @XmlElement(name = "expirationYear")
    private int expirationYear;
    @XmlElement(name = "merchantPaymentMethodId")
    private String merchantPaymentMethodId;
    @XmlElement(name = "enabled")
    private boolean enabled = true;


    public CisAddress getBillingAddress()
    {
        return this.billingAddress;
    }


    public void setBillingAddress(CisAddress billingAddress)
    {
        this.billingAddress = billingAddress;
    }


    public String getCardType()
    {
        return this.cardType;
    }


    public void setCardType(String cardType)
    {
        this.cardType = cardType;
    }


    public String getCardHolder()
    {
        return this.cardHolder;
    }


    public void setCardHolder(String cardHolder)
    {
        this.cardHolder = cardHolder;
    }


    public String getCcNumber()
    {
        return this.ccNumber;
    }


    public void setCcNumber(String ccNumber)
    {
        this.ccNumber = ccNumber;
    }


    public int getExpirationMonth()
    {
        return this.expirationMonth;
    }


    public void setExpirationMonth(int expirationMonth)
    {
        this.expirationMonth = expirationMonth;
    }


    public int getExpirationYear()
    {
        return this.expirationYear;
    }


    public void setExpirationYear(int expirationYear)
    {
        this.expirationYear = expirationYear;
    }


    public String getMerchantPaymentMethodId()
    {
        return this.merchantPaymentMethodId;
    }


    public void setMerchantPaymentMethodId(String merchantPaymentMethodId)
    {
        this.merchantPaymentMethodId = merchantPaymentMethodId;
    }


    public boolean isEnabled()
    {
        return this.enabled;
    }


    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }


    public String toString()
    {
        return "CisCreditCard [cardType=" + this.cardType + ", cardHolder=" + this.cardHolder + ", expirationMonth=" + this.expirationMonth + ", expirationYear=" + this.expirationYear + ", merchantPaymentMethodId=" + this.merchantPaymentMethodId + "]";
    }
}
