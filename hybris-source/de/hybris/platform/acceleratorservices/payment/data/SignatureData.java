package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class SignatureData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private BigDecimal amount;
    private String currency;
    private String merchantID;
    private String orderPageSerialNumber;
    private String sharedSecret;
    private String orderPageVersion;
    private String amountPublicSignature;
    private String currencyPublicSignature;
    private String transactionSignature;
    private String signedFields;


    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }


    public BigDecimal getAmount()
    {
        return this.amount;
    }


    public void setCurrency(String currency)
    {
        this.currency = currency;
    }


    public String getCurrency()
    {
        return this.currency;
    }


    public void setMerchantID(String merchantID)
    {
        this.merchantID = merchantID;
    }


    public String getMerchantID()
    {
        return this.merchantID;
    }


    public void setOrderPageSerialNumber(String orderPageSerialNumber)
    {
        this.orderPageSerialNumber = orderPageSerialNumber;
    }


    public String getOrderPageSerialNumber()
    {
        return this.orderPageSerialNumber;
    }


    public void setSharedSecret(String sharedSecret)
    {
        this.sharedSecret = sharedSecret;
    }


    public String getSharedSecret()
    {
        return this.sharedSecret;
    }


    public void setOrderPageVersion(String orderPageVersion)
    {
        this.orderPageVersion = orderPageVersion;
    }


    public String getOrderPageVersion()
    {
        return this.orderPageVersion;
    }


    public void setAmountPublicSignature(String amountPublicSignature)
    {
        this.amountPublicSignature = amountPublicSignature;
    }


    public String getAmountPublicSignature()
    {
        return this.amountPublicSignature;
    }


    public void setCurrencyPublicSignature(String currencyPublicSignature)
    {
        this.currencyPublicSignature = currencyPublicSignature;
    }


    public String getCurrencyPublicSignature()
    {
        return this.currencyPublicSignature;
    }


    public void setTransactionSignature(String transactionSignature)
    {
        this.transactionSignature = transactionSignature;
    }


    public String getTransactionSignature()
    {
        return this.transactionSignature;
    }


    public void setSignedFields(String signedFields)
    {
        this.signedFields = signedFields;
    }


    public String getSignedFields()
    {
        return this.signedFields;
    }
}
