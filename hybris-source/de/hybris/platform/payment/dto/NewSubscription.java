package de.hybris.platform.payment.dto;

import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import java.io.Serializable;

public class NewSubscription implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String subscriptionID;
    private PaymentTransactionEntryModel transactionEntry;


    public void setSubscriptionID(String subscriptionID)
    {
        this.subscriptionID = subscriptionID;
    }


    public String getSubscriptionID()
    {
        return this.subscriptionID;
    }


    public void setTransactionEntry(PaymentTransactionEntryModel transactionEntry)
    {
        this.transactionEntry = transactionEntry;
    }


    public PaymentTransactionEntryModel getTransactionEntry()
    {
        return this.transactionEntry;
    }
}
