package de.hybris.platform.payment.commands.result;

import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;

public class SubscriptionDataResult extends SubscriptionResult
{
    private BillingInfo billingInfo;
    private CardInfo card;


    public CardInfo getCard()
    {
        return this.card;
    }


    public void setCard(CardInfo card)
    {
        this.card = card;
    }


    public BillingInfo getBillingInfo()
    {
        return this.billingInfo;
    }


    public void setBillingInfo(BillingInfo billingInfo)
    {
        this.billingInfo = billingInfo;
    }
}
