package de.hybris.platform.payment.commands.request;

import de.hybris.platform.payment.dto.BasicCardInfo;
import java.math.BigDecimal;
import java.util.Currency;

public class EnrollmentCheckRequest extends AbstractRequest
{
    private final BasicCardInfo card;
    private final Currency currency;
    private final BigDecimal totalAmount;
    private String httpAccept;
    private String httpUserAgent;


    protected EnrollmentCheckRequest(String merchantTransactionCode, BasicCardInfo card, Currency currency, BigDecimal totalAmount)
    {
        super(merchantTransactionCode);
        this.card = card;
        this.currency = currency;
        this.totalAmount = totalAmount;
    }


    public EnrollmentCheckRequest(String merchantTransactionCode, BasicCardInfo card, Currency currency, BigDecimal totalAmount, String httpAccept, String httpUserAgent)
    {
        super(merchantTransactionCode);
        this.card = card;
        this.currency = currency;
        this.totalAmount = totalAmount;
        this.httpAccept = httpAccept;
        this.httpUserAgent = httpUserAgent;
    }


    public BasicCardInfo getCard()
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


    public String getHttpAccept()
    {
        return this.httpAccept;
    }


    public String getHttpUserAgent()
    {
        return this.httpUserAgent;
    }


    public void setHttpAccept(String httpAccept)
    {
        this.httpAccept = httpAccept;
    }


    public void setHttpUserAgent(String httpUserAgent)
    {
        this.httpUserAgent = httpUserAgent;
    }
}
