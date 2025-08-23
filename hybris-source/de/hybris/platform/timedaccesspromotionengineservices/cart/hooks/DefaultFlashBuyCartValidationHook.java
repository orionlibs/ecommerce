package de.hybris.platform.timedaccesspromotionengineservices.cart.hooks;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.strategies.hooks.CartValidationHook;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.timedaccesspromotionengineservices.FlashBuyService;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

public class DefaultFlashBuyCartValidationHook implements CartValidationHook
{
    private static final Logger LOG = Logger.getLogger(DefaultFlashBuyCartValidationHook.class);
    private static final String FLASHBUY_INVALID = "flashbuyinvalid";
    private static final String COUPON_NOT_VALID = "couponNotValid";
    private CouponDao couponDao;
    private CartService cartService;
    private CouponService couponService;
    private FlashBuyService flashBuyService;
    private ModelService modelService;
    private PromotionsService promotionsService;


    public void beforeValidateCart(CommerceCartParameter parameter, List<CommerceCartModification> modifications)
    {
        CartModel cart = parameter.getCart();
        List<FlashBuyCouponModel> appliedFlashBuyCoupons = getFlashBuyCouponByAppliedCouponCodes(cart);
        List<FlashBuyCouponModel> potentialFlashBuyCoupons = getFlashBuyCouponByAppliedPromotions(cart);
        if(!cart.isProcessingFlashBuyOrder())
        {
            appliedFlashBuyCoupons.forEach(c -> getCouponService().releaseCouponCode(c.getCouponId(), (AbstractOrderModel)cart));
            potentialFlashBuyCoupons.forEach(c -> {
                if(!getCouponService().redeemCoupon(c.getCouponId(), cart).getSuccess().booleanValue())
                {
                    addFlashBuyInvalidModification(cart, c.getProduct(), modifications);
                }
            });
        }
        else
        {
            appliedFlashBuyCoupons.forEach(c -> {
                if(!getCouponService().validateCouponCode(c.getCouponId(), null).getSuccess().booleanValue())
                {
                    addFlashBuyInvalidModification(cart, c.getProduct(), modifications);
                }
            });
            if(!potentialFlashBuyCoupons.isEmpty())
            {
                potentialFlashBuyCoupons.forEach(c -> {
                    if((CollectionUtils.isEmpty(appliedFlashBuyCoupons) || !appliedFlashBuyCoupons.contains(c)) && !getCouponService().redeemCoupon(c.getCouponId(), cart).getSuccess().booleanValue())
                    {
                        addFlashBuyInvalidModification(cart, c.getProduct(), modifications);
                    }
                });
                cart.setCalculated(Boolean.FALSE);
            }
            cart.setProcessingFlashBuyOrder(false);
            getModelService().save(cart);
        }
    }


    public void afterValidateCart(CommerceCartParameter parameter, List<CommerceCartModification> modifications)
    {
        removeFlashBuyCouponModification(modifications);
    }


    protected void removeFlashBuyCouponModification(List<CommerceCartModification> modifications)
    {
        if(modifications.stream().filter(value -> "flashbuyinvalid".equals(value.getStatusCode())).findFirst().isPresent())
        {
            modifications.removeIf(value -> "couponNotValid".equals(value.getStatusCode()));
        }
    }


    protected List<FlashBuyCouponModel> getFlashBuyCouponByAppliedCouponCodes(CartModel cart)
    {
        Collection<String> couponCodes = cart.getAppliedCouponCodes();
        List<FlashBuyCouponModel> flashBuyCouponList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(couponCodes))
        {
            couponCodes.forEach(code -> {
                try
                {
                    AbstractCouponModel coupon = getCouponDao().findCouponById(code);
                    if(coupon instanceof FlashBuyCouponModel)
                    {
                        flashBuyCouponList.add((FlashBuyCouponModel)coupon);
                    }
                }
                catch(ModelNotFoundException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(e);
                    }
                }
            });
        }
        return flashBuyCouponList;
    }


    protected List<FlashBuyCouponModel> getFlashBuyCouponByAppliedPromotions(CartModel cart)
    {
        List<PromotionResult> appliedProductPromotions = getPromotionsService().getPromotionResults((AbstractOrderModel)cart).getAppliedProductPromotions();
        List<PromotionResult> appliedOrderPromotions = getPromotionsService().getPromotionResults((AbstractOrderModel)cart).getAppliedOrderPromotions();
        List<PromotionResult> appliedPromotions = new ArrayList<>();
        appliedPromotions.addAll(appliedProductPromotions);
        appliedPromotions.addAll(appliedOrderPromotions);
        if(CollectionUtils.isNotEmpty(appliedPromotions))
        {
            return (List<FlashBuyCouponModel>)appliedPromotions.stream()
                            .map(p -> getFlashBuyService().getFlashBuyCouponByPromotionCode(p.getPromotion().getCode()))
                            .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    protected void addFlashBuyInvalidModification(CartModel cart, ProductModel product, List<CommerceCartModification> modifications)
    {
        CommerceCartModification modification = new CommerceCartModification();
        modification.setStatusCode("flashbuyinvalid");
        modification.setEntry(getCartService().getEntriesForProduct((AbstractOrderModel)cart, product).get(0));
        modifications.add(modification);
    }


    protected CouponDao getCouponDao()
    {
        return this.couponDao;
    }


    public void setCouponDao(CouponDao couponDao)
    {
        this.couponDao = couponDao;
    }


    protected CartService getCartService()
    {
        return this.cartService;
    }


    public void setCartService(CartService cartService)
    {
        this.cartService = cartService;
    }


    protected CouponService getCouponService()
    {
        return this.couponService;
    }


    public void setCouponService(CouponService couponService)
    {
        this.couponService = couponService;
    }


    protected FlashBuyService getFlashBuyService()
    {
        return this.flashBuyService;
    }


    public void setFlashBuyService(FlashBuyService flashBuyService)
    {
        this.flashBuyService = flashBuyService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public PromotionsService getPromotionsService()
    {
        return this.promotionsService;
    }


    public void setPromotionsService(PromotionsService promotionsService)
    {
        this.promotionsService = promotionsService;
    }
}
