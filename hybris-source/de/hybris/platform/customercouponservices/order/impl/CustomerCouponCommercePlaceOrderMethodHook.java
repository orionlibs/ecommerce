package de.hybris.platform.customercouponservices.order.impl;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customercouponservices.CustomerCouponService;
import de.hybris.platform.customercouponservices.order.CustomerCouponsPlaceOrderStrategy;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class CustomerCouponCommercePlaceOrderMethodHook implements CommercePlaceOrderMethodHook, CustomerCouponsPlaceOrderStrategy
{
    private CustomerCouponService customerCouponService;
    private SessionService sessionService;
    private static final String CUSTOMER_COUPON = "%3AcustomerCouponCode%3A";
    private static final String RELEVSNCE = "%3Arelevance";
    private static final String TEXT = "&text=";


    public void removeCouponsForCustomer(UserModel currentUser, OrderModel order)
    {
        CustomerModel customer = (CustomerModel)currentUser;
        Collection<String> appliedCoupons = order.getAppliedCouponCodes();
        if(CollectionUtils.isNotEmpty(appliedCoupons))
        {
            appliedCoupons.forEach(couponCode -> {
                getCustomerCouponService().removeCouponForCustomer(couponCode, customer);
                getCustomerCouponService().removeCouponNotificationByCode(couponCode);
            });
        }
    }


    public void updateContinueUrl()
    {
        String url = (String)getSessionService().getAttribute("session_continue_url");
        if(StringUtils.containsIgnoreCase(url, "%3AcustomerCouponCode%3A"))
        {
            String couponParam = StringUtils.substringBetween(url, "%3Arelevance", "&text=");
            getSessionService().setAttribute("session_continue_url",
                            StringUtils.replace(url, couponParam, ""));
        }
    }


    public void afterPlaceOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException
    {
        CartModel cartModel = parameter.getCart();
        UserModel currentUser = cartModel.getUser();
        OrderModel order = result.getOrder();
        removeCouponsForCustomer(currentUser, order);
        updateContinueUrl();
    }


    public void beforePlaceOrder(CommerceCheckoutParameter parameter) throws InvalidCartException
    {
    }


    public void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException
    {
    }


    protected CustomerCouponService getCustomerCouponService()
    {
        return this.customerCouponService;
    }


    public void setCustomerCouponService(CustomerCouponService customerCouponService)
    {
        this.customerCouponService = customerCouponService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
