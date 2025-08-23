package de.hybris.platform.payment.commands.request;

import java.math.BigDecimal;
import java.util.Currency;

public class VoidRequest extends AbstractRequest
{
    private final String requestId;
    private final String requestToken;
    private final String paymentProvider;
    private Currency currency;
    private BigDecimal totalAmount;


    public VoidRequest(String merchantTransactionCode, String requestId, String requestToken, String paymentProvider)
    {
        super(merchantTransactionCode);
        this.requestId = requestId;
        this.requestToken = requestToken;
        this.paymentProvider = paymentProvider;
    }


    public VoidRequest(String merchantTransactionCode, String requestId, String requestToken, String paymentProvider, Currency currency, BigDecimal totalAmount)
    {
        this(merchantTransactionCode, requestId, requestToken, paymentProvider);
        this.currency = currency;
        this.totalAmount = totalAmount;
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


    public BigDecimal getTotalAmount()
    {
        return this.totalAmount;
    }


    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }


    public Currency getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(Currency currency)
    {
        this.currency = currency;
    }
}
