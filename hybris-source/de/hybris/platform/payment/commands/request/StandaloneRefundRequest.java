package de.hybris.platform.payment.commands.request;

import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import java.math.BigDecimal;
import java.util.Currency;

public class StandaloneRefundRequest extends AbstractRequest
{
    private final String subscriptionID;
    private final BillingInfo billTo;
    private final CardInfo card;
    private final Currency currency;
    private final BigDecimal totalAmount;
    private final String paymentProvider;


    public StandaloneRefundRequest(String merchantTransactionCode, BillingInfo billTo, CardInfo card, Currency currency, BigDecimal totalAmount)
    {
        this(merchantTransactionCode, null, billTo, card, currency, totalAmount, null);
    }


    public StandaloneRefundRequest(String merchantTransactionCode, String subscriptionID, BillingInfo billTo, CardInfo card, Currency currency, BigDecimal totalAmount)
    {
        this(merchantTransactionCode, subscriptionID, billTo, card, currency, totalAmount, null);
    }


    public StandaloneRefundRequest(String merchantTransactionCode, String subscriptionID, BillingInfo billTo, CardInfo card, Currency currency, BigDecimal totalAmount, String paymentProvider)
    {
        super(merchantTransactionCode);
        this.subscriptionID = subscriptionID;
        this.billTo = billTo;
        this.card = card;
        this.currency = currency;
        this.totalAmount = totalAmount;
        this.paymentProvider = paymentProvider;
    }


    public boolean isTokenizedRequest()
    {
        return (this.subscriptionID != null);
    }


    public String getSubscriptionID()
    {
        return this.subscriptionID;
    }


    public BillingInfo getBillTo()
    {
        return this.billTo;
    }


    public CardInfo getCard()
    {
        return this.card;
    }


    public Currency getCurrency()
    {
        return this.currency;
    }


    public BigDecimal getTotalAmount()
    {
        return this.totalAmount;
    }


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }
}
