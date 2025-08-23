package de.hybris.platform.payment.commands.request;

import de.hybris.platform.payment.dto.BillingInfo;
import java.math.BigDecimal;
import java.util.Currency;

public class SubscriptionAuthorizationRequest extends AbstractRequest
{
    private final String subscriptionID;
    private final Currency currency;
    private final BigDecimal totalAmount;
    private final BillingInfo shippingInfo;
    private final String cv2;
    private final String paymentProvider;


    public SubscriptionAuthorizationRequest(String merchantTransactionCode, String subscriptionID, Currency currency, BigDecimal totalAmount, BillingInfo shippingInfo, String cv2, String paymentProvider)
    {
        super(merchantTransactionCode);
        this.subscriptionID = subscriptionID;
        this.currency = currency;
        this.totalAmount = totalAmount;
        this.shippingInfo = shippingInfo;
        this.cv2 = cv2;
        this.paymentProvider = paymentProvider;
    }


    public SubscriptionAuthorizationRequest(String merchantTransactionCode, String subscriptionID, Currency currency, BigDecimal totalAmount, BillingInfo shippingInfo, String paymentProvider)
    {
        super(merchantTransactionCode);
        this.subscriptionID = subscriptionID;
        this.currency = currency;
        this.totalAmount = totalAmount;
        this.shippingInfo = shippingInfo;
        this.cv2 = null;
        this.paymentProvider = paymentProvider;
    }


    public String getSubscriptionID()
    {
        return this.subscriptionID;
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


    public String getCv2()
    {
        return this.cv2;
    }


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }
}
