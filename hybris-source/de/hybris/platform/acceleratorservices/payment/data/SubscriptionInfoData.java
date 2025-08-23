package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;

public class SubscriptionInfoData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String subscriptionID;
    private String subscriptionIDPublicSignature;
    private String subscriptionSignedValue;


    public void setSubscriptionID(String subscriptionID)
    {
        this.subscriptionID = subscriptionID;
    }


    public String getSubscriptionID()
    {
        return this.subscriptionID;
    }


    public void setSubscriptionIDPublicSignature(String subscriptionIDPublicSignature)
    {
        this.subscriptionIDPublicSignature = subscriptionIDPublicSignature;
    }


    public String getSubscriptionIDPublicSignature()
    {
        return this.subscriptionIDPublicSignature;
    }


    public void setSubscriptionSignedValue(String subscriptionSignedValue)
    {
        this.subscriptionSignedValue = subscriptionSignedValue;
    }


    public String getSubscriptionSignedValue()
    {
        return this.subscriptionSignedValue;
    }
}
