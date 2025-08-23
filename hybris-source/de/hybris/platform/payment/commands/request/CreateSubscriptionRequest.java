package de.hybris.platform.payment.commands.request;

import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import java.util.Currency;

public class CreateSubscriptionRequest extends AbstractRequest
{
    private final BillingInfo billingInfo;
    private final Currency currency;
    private final CardInfo card;
    private final String requestId;
    private final String requestToken;
    private final String paymentProvider;


    public CreateSubscriptionRequest(String merchantTransactionCode, BillingInfo billingInfo, Currency currency, CardInfo card, String requestId, String requestToken, String paymentProvider)
    {
        super(merchantTransactionCode);
        this.billingInfo = billingInfo;
        this.currency = currency;
        this.card = card;
        this.requestId = requestId;
        this.requestToken = requestToken;
        this.paymentProvider = paymentProvider;
    }


    public CardInfo getCard()
    {
        return this.card;
    }


    public Currency getCurrency()
    {
        return this.currency;
    }


    public BillingInfo getBillingInfo()
    {
        return this.billingInfo;
    }


    public String getRequestId()
    {
        return this.requestId;
    }


    public String getRequestToken()
    {
        return this.requestToken;
    }


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }
}
