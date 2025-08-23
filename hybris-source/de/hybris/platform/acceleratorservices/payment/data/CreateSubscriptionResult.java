package de.hybris.platform.acceleratorservices.payment.data;

public class CreateSubscriptionResult extends AbstractPaymentResult
{
    private AuthReplyData authReplyData;
    private CustomerInfoData customerInfoData;
    private PaymentInfoData paymentInfoData;
    private OrderInfoData orderInfoData;
    private SignatureData signatureData;
    private SubscriptionInfoData subscriptionInfoData;
    private SubscriptionSignatureData subscriptionSignatureData;


    public void setAuthReplyData(AuthReplyData authReplyData)
    {
        this.authReplyData = authReplyData;
    }


    public AuthReplyData getAuthReplyData()
    {
        return this.authReplyData;
    }


    public void setCustomerInfoData(CustomerInfoData customerInfoData)
    {
        this.customerInfoData = customerInfoData;
    }


    public CustomerInfoData getCustomerInfoData()
    {
        return this.customerInfoData;
    }


    public void setPaymentInfoData(PaymentInfoData paymentInfoData)
    {
        this.paymentInfoData = paymentInfoData;
    }


    public PaymentInfoData getPaymentInfoData()
    {
        return this.paymentInfoData;
    }


    public void setOrderInfoData(OrderInfoData orderInfoData)
    {
        this.orderInfoData = orderInfoData;
    }


    public OrderInfoData getOrderInfoData()
    {
        return this.orderInfoData;
    }


    public void setSignatureData(SignatureData signatureData)
    {
        this.signatureData = signatureData;
    }


    public SignatureData getSignatureData()
    {
        return this.signatureData;
    }


    public void setSubscriptionInfoData(SubscriptionInfoData subscriptionInfoData)
    {
        this.subscriptionInfoData = subscriptionInfoData;
    }


    public SubscriptionInfoData getSubscriptionInfoData()
    {
        return this.subscriptionInfoData;
    }


    public void setSubscriptionSignatureData(SubscriptionSignatureData subscriptionSignatureData)
    {
        this.subscriptionSignatureData = subscriptionSignatureData;
    }


    public SubscriptionSignatureData getSubscriptionSignatureData()
    {
        return this.subscriptionSignatureData;
    }
}
