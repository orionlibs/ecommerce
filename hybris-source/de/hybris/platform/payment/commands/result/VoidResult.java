package de.hybris.platform.payment.commands.result;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class VoidResult extends AbstractResult
{
    private Currency currency;
    private BigDecimal amount;
    private Date requestTime;


    public Currency getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(Currency currency)
    {
        this.currency = currency;
    }


    public BigDecimal getAmount()
    {
        return this.amount;
    }


    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
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
