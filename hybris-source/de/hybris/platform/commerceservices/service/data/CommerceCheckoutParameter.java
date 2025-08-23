package de.hybris.platform.commerceservices.service.data;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import java.io.Serializable;
import java.math.BigDecimal;

public class CommerceCheckoutParameter implements Serializable
{
    private static final long serialVersionUID = 1L;
    private CartModel cart;
    private AddressModel address;
    private boolean isDeliveryAddress;
    private DeliveryModeModel deliveryMode;
    private PaymentInfoModel paymentInfo;
    private String securityCode;
    private String paymentProvider;
    private BigDecimal authorizationAmount;
    private SalesApplication salesApplication;
    private boolean enableHooks;


    public void setCart(CartModel cart)
    {
        this.cart = cart;
    }


    public CartModel getCart()
    {
        return this.cart;
    }


    public void setAddress(AddressModel address)
    {
        this.address = address;
    }


    public AddressModel getAddress()
    {
        return this.address;
    }


    public void setIsDeliveryAddress(boolean isDeliveryAddress)
    {
        this.isDeliveryAddress = isDeliveryAddress;
    }


    public boolean isIsDeliveryAddress()
    {
        return this.isDeliveryAddress;
    }


    public void setDeliveryMode(DeliveryModeModel deliveryMode)
    {
        this.deliveryMode = deliveryMode;
    }


    public DeliveryModeModel getDeliveryMode()
    {
        return this.deliveryMode;
    }


    public void setPaymentInfo(PaymentInfoModel paymentInfo)
    {
        this.paymentInfo = paymentInfo;
    }


    public PaymentInfoModel getPaymentInfo()
    {
        return this.paymentInfo;
    }


    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
    }


    public String getSecurityCode()
    {
        return this.securityCode;
    }


    public void setPaymentProvider(String paymentProvider)
    {
        this.paymentProvider = paymentProvider;
    }


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }


    public void setAuthorizationAmount(BigDecimal authorizationAmount)
    {
        this.authorizationAmount = authorizationAmount;
    }


    public BigDecimal getAuthorizationAmount()
    {
        return this.authorizationAmount;
    }


    public void setSalesApplication(SalesApplication salesApplication)
    {
        this.salesApplication = salesApplication;
    }


    public SalesApplication getSalesApplication()
    {
        return this.salesApplication;
    }


    public void setEnableHooks(boolean enableHooks)
    {
        this.enableHooks = enableHooks;
    }


    public boolean isEnableHooks()
    {
        return this.enableHooks;
    }
}
