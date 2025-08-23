package de.hybris.platform.timedaccesspromotionengineservices.order.hooks;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.redemption.strategies.CouponRedemptionStrategy;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.timedaccesspromotionengineservices.FlashBuyService;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class DefaultFlashBuyCommercePlaceOrderMethodHook implements CommercePlaceOrderMethodHook
{
    private FlashBuyService flashBuyService;
    private CouponRedemptionStrategy<FlashBuyCouponModel> couponRedemptionStrategy;


    public void afterPlaceOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException
    {
        OrderModel order = result.getOrder();
        order.getEntries()
                        .stream()
                        .forEach(orderEntry -> {
                            List<PromotionSourceRuleModel> promotionSourceRules = getFlashBuyService().getPromotionSourceRulesByProductCode(orderEntry.getProduct().getCode());
                            if(CollectionUtils.isNotEmpty(promotionSourceRules))
                            {
                                promotionSourceRules.stream().forEach(());
                            }
                        });
    }


    protected boolean isFlashBuyCouponCompleted(String code, FlashBuyCouponModel flashBuyCoupon)
    {
        ServicesUtil.validateParameterNotNull(code, "Code must not be null");
        return !getCouponRedemptionStrategy().isCouponRedeemable((AbstractCouponModel)flashBuyCoupon, null, flashBuyCoupon.getCouponId());
    }


    public void beforePlaceOrder(CommerceCheckoutParameter parameter) throws InvalidCartException
    {
    }


    public void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException
    {
    }


    protected FlashBuyService getFlashBuyService()
    {
        return this.flashBuyService;
    }


    public void setFlashBuyService(FlashBuyService flashBuyService)
    {
        this.flashBuyService = flashBuyService;
    }


    protected CouponRedemptionStrategy<FlashBuyCouponModel> getCouponRedemptionStrategy()
    {
        return this.couponRedemptionStrategy;
    }


    public void setCouponRedemptionStrategy(CouponRedemptionStrategy<FlashBuyCouponModel> couponRedemptionStrategy)
    {
        this.couponRedemptionStrategy = couponRedemptionStrategy;
    }
}
