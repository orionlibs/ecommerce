package de.hybris.platform.payment.commands.result;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class RefundResult extends AbstractResult
{
    private Currency currency;
    private BigDecimal totalAmount;
    private Date requestTime;
    private String paymentProvider;


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }


    public void setPaymentProvider(String paymentProvider)
    {
        this.paymentProvider = paymentProvider;
    }


    public Currency getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(Currency currency)
    {
        this.currency = currency;
    }


    public BigDecimal getTotalAmount()
    {
        return this.totalAmount;
    }


    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }


    public Date getRequestTime()
    {
        return this.requestTime;
    }


    public void setRequestTime(Date requestTime)
    {
        this.requestTime = requestTime;
    }
}
