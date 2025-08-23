package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.CisResult;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "subscriptionTransactionResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionTransactionResult extends CisResult
{
    @XmlTransient
    private CisSubscriptionRequest request;
    @XmlElement(name = "amount")
    private BigDecimal amount;
    @XmlElement(name = "sessionTransactionToken")
    private String sessionTransactionToken;
    private transient String clientAuthorizationId;
    @XmlElement(name = "merchantProductId")
    private String merchantProductId;


    public CisSubscriptionRequest getRequest()
    {
        return this.request;
    }


    public void setRequest(CisSubscriptionRequest request)
    {
        this.request = request;
    }


    public BigDecimal getAmount()
    {
        return this.amount;
    }


    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }


    public String getClientAuthorizationId()
    {
        return this.clientAuthorizationId;
    }


    public void setClientAuthorizationId(String clientAuthorizationId)
    {
        this.clientAuthorizationId = clientAuthorizationId;
    }


    public String toString()
    {
        return "CisSubscriptionTransactionResult [request=" + this.request + ", amount=" + this.amount + ", merchantProductId=" + this.merchantProductId + ", transactionVerificationKey=" + this.sessionTransactionToken + ", clientAuthorizationId=" + this.clientAuthorizationId + ", decision=" +
                        getDecision() + ", id=" +
                        getId() + ", vendorReasonCode=" + getVendorReasonCode() + ", vendorStatusCode=" +
                        getVendorStatusCode() + ", clientRefId=" + getClientRefId() + "]";
    }


    public String getMerchantProductId()
    {
        return this.merchantProductId;
    }


    public void setMerchantProductId(String merchantProductId)
    {
        this.merchantProductId = merchantProductId;
    }


    public String getSessionTransactionToken()
    {
        return this.sessionTransactionToken;
    }


    public void setSessionTransactionToken(String sessionTransactionToken)
    {
        this.sessionTransactionToken = sessionTransactionToken;
    }
}
