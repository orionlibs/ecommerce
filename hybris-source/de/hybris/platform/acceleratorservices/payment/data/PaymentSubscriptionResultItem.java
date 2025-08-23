package de.hybris.platform.acceleratorservices.payment.data;

import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;

public class PaymentSubscriptionResultItem extends PaymentSubscriptionResult
{
    private CreditCardPaymentInfoModel storedCard;


    public void setStoredCard(CreditCardPaymentInfoModel storedCard)
    {
        this.storedCard = storedCard;
    }


    public CreditCardPaymentInfoModel getStoredCard()
    {
        return this.storedCard;
    }
}
