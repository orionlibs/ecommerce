package de.hybris.platform.payment.commands.request;

import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;

public class UpdateSubscriptionRequest extends AbstractRequest
{
    private final String subscriptionID;
    private final String paymentProvider;
    private final BillingInfo billingInfo;
    private final CardInfo card;


    public UpdateSubscriptionRequest(String merchantTransactionCode, String subscriptionID, String paymentProvider, BillingInfo billingInfo, CardInfo card)
    {
        super(merchantTransactionCode);
        this.subscriptionID = subscriptionID;
        this.paymentProvider = paymentProvider;
        this.billingInfo = billingInfo;
        this.card = card;
    }


    public String getSubscriptionID()
    {
        return this.subscriptionID;
    }


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }


    public CardInfo getCard()
    {
        return this.card;
    }


    public BillingInfo getBillingInfo()
    {
        return this.billingInfo;
    }
}
