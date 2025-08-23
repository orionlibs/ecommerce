package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.AnnotationHashMap;
import com.hybris.cis.api.model.CisLineItem;
import com.hybris.cis.api.validation.XSSSafe;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionProcessPayNowRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionPayNowRequest extends CisSubscriptionRequest
{
    @XmlElement(name = "amount")
    private BigDecimal amount;
    @XmlElement(name = "currency")
    @XSSSafe
    private String currency;
    @XmlElement(name = "profileId")
    @XSSSafe
    private String profileId;
    @XmlElement(name = "paymentMethodId")
    @XSSSafe
    private String paymentMethodId;
    @XmlElement(name = "merchantTransactionId")
    @XSSSafe
    private String merchantTransactionId;
    @XmlElement(name = "merchantTransactionDescription")
    @XSSSafe
    private String merchantTransactionDescription;
    @XmlElementWrapper(name = "lineItems")
    @XmlElement(name = "lineItem")
    @Valid
    private List<CisLineItem> lineItems = new ArrayList<>();
    @XmlElement(name = "transactionMode")
    @XSSSafe
    private String transactionMode;
    @XmlElement(name = "vendorParameters")
    @Valid
    private AnnotationHashMap parameters = new AnnotationHashMap();


    public CisSubscriptionPayNowRequest()
    {
    }


    public CisSubscriptionPayNowRequest(CisSubscriptionPayNowRequest request)
    {
        this.amount = request.getAmount();
        this.currency = request.getCurrency();
        this.merchantTransactionDescription = request.getMerchantTransactionDescription();
        this.merchantTransactionId = request.getMerchantTransactionId();
        this.paymentMethodId = request.getPaymentMethodId();
        this.profileId = request.getProfileId();
        this.transactionMode = request.getTransactionMode();
    }


    public BigDecimal getAmount()
    {
        return this.amount;
    }


    public void setAmount(BigDecimal value)
    {
        this.amount = value;
    }


    public String getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(String currency)
    {
        this.currency = currency;
    }


    public String toString()
    {
        return "CisPaymentRequest [amount=" + getAmount() + ", currency=" + getCurrency() + "]";
    }


    public AnnotationHashMap getParameters()
    {
        return this.parameters;
    }


    public void setParameters(AnnotationHashMap parameters)
    {
        this.parameters = parameters;
    }


    public String getProfileId()
    {
        return this.profileId;
    }


    public void setProfileId(String profileId)
    {
        this.profileId = profileId;
    }


    public String getPaymentMethodId()
    {
        return this.paymentMethodId;
    }


    public void setPaymentMethodId(String paymentMethodId)
    {
        this.paymentMethodId = paymentMethodId;
    }


    public String getMerchantTransactionId()
    {
        return this.merchantTransactionId;
    }


    public void setMerchantTransactionId(String merchantTransactionId)
    {
        this.merchantTransactionId = merchantTransactionId;
    }


    public String getMerchantTransactionDescription()
    {
        return this.merchantTransactionDescription;
    }


    public void setMerchantTransactionDescription(String merchantTransactionDescription)
    {
        this.merchantTransactionDescription = merchantTransactionDescription;
    }


    public String getTransactionMode()
    {
        return this.transactionMode;
    }


    public void setTransactionMode(String transactionMode)
    {
        this.transactionMode = transactionMode;
    }


    public List<CisLineItem> getLineItems()
    {
        return this.lineItems;
    }


    public void setLineItems(List<CisLineItem> lineItems)
    {
        this.lineItems = lineItems;
    }
}
