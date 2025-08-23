package de.hybris.platform.acceleratorfacades.payment.data;

import de.hybris.platform.acceleratorservices.payment.data.PaymentSubscriptionResult;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;

public class PaymentSubscriptionResultData extends PaymentSubscriptionResult
{
    private CCPaymentInfoData storedCard;


    public void setStoredCard(CCPaymentInfoData storedCard)
    {
        this.storedCard = storedCard;
    }


    public CCPaymentInfoData getStoredCard()
    {
        return this.storedCard;
    }
}
