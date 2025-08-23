package de.hybris.platform.payment.commands.request;

import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import java.math.BigDecimal;
import java.util.Currency;

public class AuthorizationRequest extends AbstractRequest
{
    private final CardInfo card;
    private final Currency currency;
    private final BigDecimal totalAmount;
    private final BillingInfo shippingInfo;


    public AuthorizationRequest(String merchantTransactionCode, CardInfo card, Currency currency, BigDecimal totalAmount, BillingInfo shippingInfo)
    {
        super(merchantTransactionCode);
        this.card = card;
        this.currency = currency;
        this.totalAmount = totalAmount;
        this.shippingInfo = shippingInfo;
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


    public BillingInfo getShippingInfo()
    {
        return this.shippingInfo;
    }
}
