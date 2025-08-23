package de.hybris.platform.payment.commands.result;

public class SubscriptionResult extends AbstractResult
{
    private String subscriptionID;


    public String getSubscriptionID()
    {
        return this.subscriptionID;
    }


    public void setSubscriptionID(String subscriptionID)
    {
        this.subscriptionID = subscriptionID;
    }
}
