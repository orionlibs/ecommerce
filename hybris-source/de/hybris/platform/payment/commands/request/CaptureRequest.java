package de.hybris.platform.payment.commands.request;

import java.math.BigDecimal;
import java.util.Currency;

public class CaptureRequest extends AbstractRequest
{
    private final String requestId;
    private final String requestToken;
    private final Currency currency;
    private final BigDecimal totalAmount;
    private final String paymentProvider;
    private final String subscriptionID;


    public CaptureRequest(String merchantTransactionCode, String requestId, String requestToken, Currency currency, BigDecimal totalAmount, String paymentProvider)
    {
        this(merchantTransactionCode, requestId, requestToken, currency, totalAmount, paymentProvider, null);
    }


    public CaptureRequest(String merchantTransactionCode, String requestId, String requestToken, Currency currency, BigDecimal totalAmount, String paymentProvider, String subscriptionID)
    {
        super(merchantTransactionCode);
        this.requestId = requestId;
        this.requestToken = requestToken;
        this.currency = currency;
        this.totalAmount = totalAmount;
        this.paymentProvider = paymentProvider;
        this.subscriptionID = subscriptionID;
    }


    public String getRequestId()
    {
        return this.requestId;
    }


    public String getRequestToken()
    {
        return this.requestToken;
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


    public String getSubscriptionID()
    {
        return this.subscriptionID;
    }
}
