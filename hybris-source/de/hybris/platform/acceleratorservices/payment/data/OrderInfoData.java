package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;

public class OrderInfoData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String comments;
    private String orderNumber;
    private Boolean orderPageIgnoreAVS;
    private Boolean orderPageIgnoreCVN;
    private String orderPageRequestToken;
    private String orderPageTransactionType;
    private String recurringSubscriptionInfoPublicSignature;
    private String subscriptionTitle;
    private String taxAmount;


    public void setComments(String comments)
    {
        this.comments = comments;
    }


    public String getComments()
    {
        return this.comments;
    }


    public void setOrderNumber(String orderNumber)
    {
        this.orderNumber = orderNumber;
    }


    public String getOrderNumber()
    {
        return this.orderNumber;
    }


    public void setOrderPageIgnoreAVS(Boolean orderPageIgnoreAVS)
    {
        this.orderPageIgnoreAVS = orderPageIgnoreAVS;
    }


    public Boolean getOrderPageIgnoreAVS()
    {
        return this.orderPageIgnoreAVS;
    }


    public void setOrderPageIgnoreCVN(Boolean orderPageIgnoreCVN)
    {
        this.orderPageIgnoreCVN = orderPageIgnoreCVN;
    }


    public Boolean getOrderPageIgnoreCVN()
    {
        return this.orderPageIgnoreCVN;
    }


    public void setOrderPageRequestToken(String orderPageRequestToken)
    {
        this.orderPageRequestToken = orderPageRequestToken;
    }


    public String getOrderPageRequestToken()
    {
        return this.orderPageRequestToken;
    }


    public void setOrderPageTransactionType(String orderPageTransactionType)
    {
        this.orderPageTransactionType = orderPageTransactionType;
    }


    public String getOrderPageTransactionType()
    {
        return this.orderPageTransactionType;
    }


    public void setRecurringSubscriptionInfoPublicSignature(String recurringSubscriptionInfoPublicSignature)
    {
        this.recurringSubscriptionInfoPublicSignature = recurringSubscriptionInfoPublicSignature;
    }


    public String getRecurringSubscriptionInfoPublicSignature()
    {
        return this.recurringSubscriptionInfoPublicSignature;
    }


    public void setSubscriptionTitle(String subscriptionTitle)
    {
        this.subscriptionTitle = subscriptionTitle;
    }


    public String getSubscriptionTitle()
    {
        return this.subscriptionTitle;
    }


    public void setTaxAmount(String taxAmount)
    {
        this.taxAmount = taxAmount;
    }


    public String getTaxAmount()
    {
        return this.taxAmount;
    }
}
