package de.hybris.platform.acceleratorservices.payment.data;

public class CreateSubscriptionRequest extends HostedOrderPageRequest
{
    private CustomerBillToData customerBillToData;
    private CustomerShipToData customerShipToData;
    private PaymentInfoData paymentInfoData;
    private OrderInfoData orderInfoData;
    private OrderPageAppearanceData orderPageAppearanceData;
    private OrderPageConfirmationData orderPageConfirmationData;
    private SignatureData signatureData;
    private SubscriptionSignatureData subscriptionSignatureData;


    public void setCustomerBillToData(CustomerBillToData customerBillToData)
    {
        this.customerBillToData = customerBillToData;
    }


    public CustomerBillToData getCustomerBillToData()
    {
        return this.customerBillToData;
    }


    public void setCustomerShipToData(CustomerShipToData customerShipToData)
    {
        this.customerShipToData = customerShipToData;
    }


    public CustomerShipToData getCustomerShipToData()
    {
        return this.customerShipToData;
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


    public void setOrderPageAppearanceData(OrderPageAppearanceData orderPageAppearanceData)
    {
        this.orderPageAppearanceData = orderPageAppearanceData;
    }


    public OrderPageAppearanceData getOrderPageAppearanceData()
    {
        return this.orderPageAppearanceData;
    }


    public void setOrderPageConfirmationData(OrderPageConfirmationData orderPageConfirmationData)
    {
        this.orderPageConfirmationData = orderPageConfirmationData;
    }


    public OrderPageConfirmationData getOrderPageConfirmationData()
    {
        return this.orderPageConfirmationData;
    }


    public void setSignatureData(SignatureData signatureData)
    {
        this.signatureData = signatureData;
    }


    public SignatureData getSignatureData()
    {
        return this.signatureData;
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
