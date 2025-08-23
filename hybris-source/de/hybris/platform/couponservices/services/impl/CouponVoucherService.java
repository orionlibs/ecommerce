package de.hybris.platform.couponservices.services.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.voucher.impl.DefaultVoucherService;
import java.util.Collection;
import java.util.Collections;

public class CouponVoucherService extends DefaultVoucherService
{
    public Collection<String> getAppliedVoucherCodes(CartModel cart)
    {
        return (cart.getAppliedCouponCodes() == null) ? Collections.<String>emptyList() : cart.getAppliedCouponCodes();
    }


    public Collection<String> getAppliedVoucherCodes(OrderModel order)
    {
        return (order.getAppliedCouponCodes() == null) ? Collections.<String>emptyList() : order.getAppliedCouponCodes();
    }
}
