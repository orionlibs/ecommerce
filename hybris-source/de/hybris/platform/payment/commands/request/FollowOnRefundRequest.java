package de.hybris.platform.payment.commands.request;

import java.math.BigDecimal;
import java.util.Currency;

public class FollowOnRefundRequest extends AbstractRequest
{
    private final String requestId;
    private final String requestToken;
    private final Currency currency;
    private final BigDecimal totalAmount;
    private final String paymentProvider;


    public FollowOnRefundRequest(String merchantTransactionCode, String requestId, String requestToken, Currency currency, BigDecimal totalAmount, String paymentProvider)
    {
        super(merchantTransactionCode);
        this.requestId = requestId;
        this.requestToken = requestToken;
        this.currency = currency;
        this.totalAmount = totalAmount;
        this.paymentProvider = paymentProvider;
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
}
