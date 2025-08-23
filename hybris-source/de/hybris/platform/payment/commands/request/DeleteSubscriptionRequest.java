package de.hybris.platform.payment.commands.request;

public class DeleteSubscriptionRequest extends AbstractRequest
{
    private final String subscriptionID;
    private final String paymentProvider;


    public DeleteSubscriptionRequest(String merchantTransactionCode, String subscriptionID, String paymentProvider)
    {
        super(merchantTransactionCode);
        this.subscriptionID = subscriptionID;
        this.paymentProvider = paymentProvider;
    }


    public String getSubscriptionID()
    {
        return this.subscriptionID;
    }


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }
}
