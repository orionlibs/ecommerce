package de.hybris.platform.couponservices.services.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.CouponServiceException;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import de.hybris.platform.couponservices.services.CouponCodeGenerationService;
import de.hybris.platform.couponservices.services.CouponManagementService;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCouponService implements CouponService
{
    public static final String COUPON_CODE = "couponCode";
    public static final String ORDER = "order";
    private CouponManagementService couponManagementService;
    private CalculationService calculationService;
    private PromotionsService promotionsService;
    private BaseSiteService baseSiteService;
    private ModelService modelService;
    private CouponCodeGenerationService couponCodeGenerationService;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCouponService.class);
    private static final String COUPONCODECANNOTBENULL = "Coupon code cannot be NULL here";


    public CouponResponse verifyCouponCode(String couponCode, AbstractOrderModel order)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("couponCode", couponCode);
        ServicesUtil.validateParameterNotNullStandardMessage("order", order);
        return getCouponManagementService().verifyCouponCode(couponCode, order);
    }


    public CouponResponse validateCouponCode(String couponCode, UserModel user)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("couponCode", couponCode);
        return getCouponManagementService().validateCouponCode(couponCode, user);
    }


    public CouponResponse redeemCoupon(String couponCode, CartModel cart)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("couponCode", couponCode);
        ServicesUtil.validateParameterNotNullStandardMessage("cart", cart);
        String clearedCouponCode = clearCouponCode(couponCode);
        CouponResponse response = assertCouponCodeInOrder(clearedCouponCode, (AbstractOrderModel)cart);
        if(BooleanUtils.isTrue(response.getSuccess()))
        {
            redeemCouponCode(cart, clearedCouponCode, response);
        }
        return response;
    }


    protected void redeemCouponCode(CartModel cart, String clearedCouponCode, CouponResponse response)
    {
        try
        {
            if(getCouponManagementService().redeem(clearedCouponCode, cart))
            {
                Set<String> codes = new LinkedHashSet<>();
                if(CollectionUtils.isNotEmpty(cart.getAppliedCouponCodes()))
                {
                    codes.addAll(cart.getAppliedCouponCodes());
                }
                codes.add(clearedCouponCode);
                cart.setAppliedCouponCodes(codes);
                getModelService().save(cart);
                recalculateOrder((AbstractOrderModel)cart);
            }
        }
        catch(CouponServiceException ex)
        {
            LOG.debug(ex.getMessage(), (Throwable)ex);
            response.setSuccess(Boolean.FALSE);
            response.setMessage(ex.getMessage());
        }
    }


    public CouponResponse redeemCoupon(String couponCode, OrderModel order)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("couponCode", couponCode);
        ServicesUtil.validateParameterNotNullStandardMessage("order", order);
        String clearedCouponCode = clearCouponCode(couponCode);
        if(!containsCouponCode(clearedCouponCode, (AbstractOrderModel)order))
        {
            throw new CouponServiceException("Cannot redeem couponCode '" + couponCode + "'. It has not been applied to order " + order
                            .getCode());
        }
        return getCouponManagementService().redeem(clearedCouponCode, order);
    }


    protected String clearCouponCode(String couponCode)
    {
        return couponCode.trim();
    }


    protected CouponResponse assertCouponCodeInOrder(String couponCode, AbstractOrderModel order)
    {
        CouponResponse response = new CouponResponse();
        response.setSuccess(Boolean.TRUE);
        if(containsCouponCode(couponCode, order))
        {
            response.setMessage("coupon.already.exists.cart");
            response.setSuccess(Boolean.FALSE);
        }
        return response;
    }


    protected boolean containsCouponCode(String couponCode, AbstractOrderModel order)
    {
        if(CollectionUtils.isNotEmpty(order.getAppliedCouponCodes()))
        {
            Optional<AbstractCouponModel> couponModel = getCouponManagementService().getCouponForCode(couponCode);
            if(couponModel.isPresent())
            {
                return order.getAppliedCouponCodes().stream().anyMatch(checkMatch(couponModel.get(), couponCode));
            }
        }
        return false;
    }


    protected Predicate<String> checkMatch(AbstractCouponModel coupon, String couponCode)
    {
        if(coupon instanceof de.hybris.platform.couponservices.model.MultiCodeCouponModel)
        {
            return appliedCouponCode -> {
                String couponPrefix = getCouponCodeGenerationService().extractCouponPrefix(couponCode);
                return (couponPrefix != null && couponPrefix.equals(getCouponCodeGenerationService().extractCouponPrefix(appliedCouponCode)));
            };
        }
        return appliedCouponCode -> appliedCouponCode.equals(couponCode);
    }


    protected void recalculateOrder(AbstractOrderModel order)
    {
        try
        {
            getCalculationService().calculate(order);
            getPromotionsService().updatePromotions(getPromotionGroups(), order);
        }
        catch(CalculationException e)
        {
            LOG.error("Error re-calculating the order", (Throwable)e);
            throw new CouponServiceException("coupon.order.recalculation.error");
        }
    }


    public Optional<AbstractCouponModel> getCouponForCode(String couponCode)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(couponCode), "Coupon code cannot be NULL here");
        return getCouponManagementService().getCouponForCode(couponCode);
    }


    public Optional<AbstractCouponModel> getValidatedCouponForCode(String couponCode)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(couponCode), "Coupon code cannot be NULL here");
        return getCouponManagementService().getValidatedCouponForCode(couponCode);
    }


    public void releaseCouponCode(String couponCode, AbstractOrderModel order)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("couponCode", couponCode);
        ServicesUtil.validateParameterNotNullStandardMessage("order", order);
        getCouponManagementService().releaseCouponCode(couponCode);
        removeCouponAndTriggerCalculation(couponCode, order);
    }


    protected void removeCouponAndTriggerCalculation(String couponCode, AbstractOrderModel order)
    {
        Collection<String> couponCodes = order.getAppliedCouponCodes();
        if(CollectionUtils.isNotEmpty(couponCodes) && containsCouponCode(couponCode, order))
        {
            Set<String> couponCodesFiltered = (Set<String>)couponCodes.stream().filter(c -> !c.equals(couponCode)).collect(Collectors.toSet());
            order.setAppliedCouponCodes(couponCodesFiltered);
            order.setCalculated(Boolean.FALSE);
            getModelService().save(order);
            recalculateOrder(order);
        }
    }


    protected Collection<PromotionGroupModel> getPromotionGroups()
    {
        Collection<PromotionGroupModel> promotionGroupModels = new ArrayList<>();
        BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();
        if(Objects.nonNull(currentBaseSite) && Objects.nonNull(currentBaseSite.getDefaultPromotionGroup()))
        {
            promotionGroupModels.add(currentBaseSite.getDefaultPromotionGroup());
        }
        return promotionGroupModels;
    }


    protected CouponManagementService getCouponManagementService()
    {
        return this.couponManagementService;
    }


    @Required
    public void setCouponManagementService(CouponManagementService couponManagementService)
    {
        this.couponManagementService = couponManagementService;
    }


    protected CalculationService getCalculationService()
    {
        return this.calculationService;
    }


    @Required
    public void setCalculationService(CalculationService calculationService)
    {
        this.calculationService = calculationService;
    }


    protected PromotionsService getPromotionsService()
    {
        return this.promotionsService;
    }


    @Required
    public void setPromotionsService(PromotionsService promotionsService)
    {
        this.promotionsService = promotionsService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected CouponCodeGenerationService getCouponCodeGenerationService()
    {
        return this.couponCodeGenerationService;
    }


    @Required
    public void setCouponCodeGenerationService(CouponCodeGenerationService couponCodeGenerationService)
    {
        this.couponCodeGenerationService = couponCodeGenerationService;
    }
}
