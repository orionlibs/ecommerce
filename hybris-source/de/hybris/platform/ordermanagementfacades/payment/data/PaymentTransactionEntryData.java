package de.hybris.platform.ordermanagementfacades.payment.data;

import de.hybris.platform.payment.enums.PaymentTransactionType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PaymentTransactionEntryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private BigDecimal amount;
    private String code;
    private String currencyIsocode;
    private String requestId;
    private String requestToken;
    private String subscriptionID;
    private Date time;
    private String transactionStatus;
    private String transactionStatusDetails;
    private PaymentTransactionType type;
    private String versionID;


    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }


    public BigDecimal getAmount()
    {
        return this.amount;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCurrencyIsocode(String currencyIsocode)
    {
        this.currencyIsocode = currencyIsocode;
    }


    public String getCurrencyIsocode()
    {
        return this.currencyIsocode;
    }


    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }


    public String getRequestId()
    {
        return this.requestId;
    }


    public void setRequestToken(String requestToken)
    {
        this.requestToken = requestToken;
    }


    public String getRequestToken()
    {
        return this.requestToken;
    }


    public void setSubscriptionID(String subscriptionID)
    {
        this.subscriptionID = subscriptionID;
    }


    public String getSubscriptionID()
    {
        return this.subscriptionID;
    }


    public void setTime(Date time)
    {
        this.time = time;
    }


    public Date getTime()
    {
        return this.time;
    }


    public void setTransactionStatus(String transactionStatus)
    {
        this.transactionStatus = transactionStatus;
    }


    public String getTransactionStatus()
    {
        return this.transactionStatus;
    }


    public void setTransactionStatusDetails(String transactionStatusDetails)
    {
        this.transactionStatusDetails = transactionStatusDetails;
    }


    public String getTransactionStatusDetails()
    {
        return this.transactionStatusDetails;
    }


    public void setType(PaymentTransactionType type)
    {
        this.type = type;
    }


    public PaymentTransactionType getType()
    {
        return this.type;
    }


    public void setVersionID(String versionID)
    {
        this.versionID = versionID;
    }


    public String getVersionID()
    {
        return this.versionID;
    }
}
