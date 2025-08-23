package de.hybris.platform.couponservices.util;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.ruleengineservices.ruleengine.impl.CartTestContextBuilder;
import java.util.Set;

public class CouponAwareCartTestContextBuilder extends CartTestContextBuilder
{
    public CartTestContextBuilder withCouponCodes(Set<String> couponCodes)
    {
        CartModel cartModel = getCartModel();
        cartModel.setAppliedCouponCodes(couponCodes);
        return this;
    }
}
