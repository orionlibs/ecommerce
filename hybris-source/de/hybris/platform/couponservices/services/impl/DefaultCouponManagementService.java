package de.hybris.platform.couponservices.services.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.CouponServiceException;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.model.CouponRedemptionModel;
import de.hybris.platform.couponservices.redemption.strategies.CouponRedemptionStrategy;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import de.hybris.platform.couponservices.services.CouponManagementService;
import de.hybris.platform.couponservices.strategies.FindCouponStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCouponManagementService implements CouponManagementService
{
    private ModelService modelService;
    private Map<String, CouponRedemptionStrategy> redemptionStrategyMap;
    private List<FindCouponStrategy> findCouponStrategiesList;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCouponManagementService.class);
    private static final String COUPONCODENOTPROVIDED = "Coupon code is not provided";


    public Optional<AbstractCouponModel> getCouponForCode(String couponCode)
    {
        return getCouponForCode(couponCode, findCouponStrategy -> findCouponStrategy.findCouponForCouponCode(couponCode));
    }


    public Optional<AbstractCouponModel> getValidatedCouponForCode(String couponCode)
    {
        try
        {
            return getCouponForCode(couponCode, findCouponStrategy -> findCouponStrategy.findValidatedCouponForCouponCode(couponCode));
        }
        catch(CouponServiceException ex)
        {
            LOG.debug(ex.getMessage(), (Throwable)ex);
            return Optional.empty();
        }
    }


    protected Optional<AbstractCouponModel> getCouponForCode(String couponCode, Function<FindCouponStrategy, Optional<AbstractCouponModel>> findCouponFunction)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(couponCode), "Coupon code is not provided");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(getFindCouponStrategiesList()), "Find Coupon Strategies is not provided");
        return getFindCouponStrategiesList().stream().<Optional<AbstractCouponModel>>map(findCouponFunction).filter(Optional::isPresent).map(Optional::get)
                        .findFirst();
    }


    public CouponResponse verifyCouponCode(String couponCode, AbstractOrderModel abstractOrder)
    {
        return validateCouponCode(couponCode, abstractOrder.getUser());
    }


    public CouponResponse validateCouponCode(String couponCode, UserModel user)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(couponCode), "Coupon code is not provided");
        CouponResponse response = new CouponResponse();
        try
        {
            Optional<AbstractCouponModel> optional = findValidatedCoupon(couponCode, user);
            if(optional.isPresent())
            {
                response.setSuccess(Boolean.TRUE);
                response.setCouponId(((AbstractCouponModel)optional.get()).getCouponId());
                return response;
            }
            response.setCouponId(couponCode);
            response.setSuccess(Boolean.FALSE);
            response.setMessage("coupon.general.error");
        }
        catch(CouponServiceException ex)
        {
            LOG.debug(ex.getMessage(), (Throwable)ex);
            response.setCouponId(couponCode);
            response.setSuccess(Boolean.FALSE);
            response.setMessage(ex.getMessage());
        }
        return response;
    }


    protected Optional<AbstractCouponModel> findCoupon(String couponCode, AbstractOrderModel order)
    {
        return findValidatedCoupon(couponCode, order.getUser());
    }


    protected Optional<AbstractCouponModel> findValidatedCoupon(String couponCode, UserModel user)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(couponCode), "Coupon code is not provided");
        for(FindCouponStrategy findCouponStrategy : getFindCouponStrategiesList())
        {
            Optional<AbstractCouponModel> optional = findCouponStrategy.findValidatedCouponForCouponCode(couponCode);
            if(optional.isPresent())
            {
                return checkCouponRedeemability(optional.get(), user, couponCode);
            }
        }
        throw new CouponServiceException("coupon.invalid.code.provided");
    }


    protected Optional<AbstractCouponModel> checkRedeemability(AbstractCouponModel coupon, AbstractOrderModel order, String couponCode)
    {
        return checkCouponRedeemability(coupon, order.getUser(), couponCode);
    }


    protected Optional<AbstractCouponModel> checkCouponRedeemability(AbstractCouponModel coupon, UserModel user, String couponCode)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("coupon", coupon);
        CouponRedemptionStrategy strategy = getRedemptionStrategyMap().get(coupon.getItemtype());
        if(Objects.isNull(strategy))
        {
            throw new IllegalArgumentException("coupon " + coupon
                            .getCouponId() + " of type:" + coupon.getItemtype() + " has no redemption strategy defined.");
        }
        if(strategy.isCouponRedeemable(coupon, user, couponCode))
        {
            return Optional.of(coupon);
        }
        throw new CouponServiceException("coupon.already.redeemed");
    }


    public void releaseCouponCode(String couponCode)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(couponCode), "Coupon code to be released is not provided");
    }


    public boolean redeem(String couponCode, CartModel cart)
    {
        CouponResponse response = verifyCouponCode(couponCode, (AbstractOrderModel)cart);
        if(BooleanUtils.isTrue(response.getSuccess()))
        {
            return true;
        }
        throw new CouponServiceException(response.getMessage());
    }


    public CouponResponse redeem(String couponCode, OrderModel order)
    {
        CouponResponse response = verifyCouponCode(couponCode, (AbstractOrderModel)order);
        if(BooleanUtils.isTrue(response.getSuccess()))
        {
            createCouponRedemption(couponCode, order);
        }
        return response;
    }


    protected void createCouponRedemption(String couponCode, OrderModel order)
    {
        CouponRedemptionModel couponRedemption = (CouponRedemptionModel)getModelService().create(CouponRedemptionModel.class);
        findCoupon(couponCode, (AbstractOrderModel)order).ifPresent(c -> couponRedemption.setCoupon(c));
        couponRedemption.setCouponCode(couponCode);
        couponRedemption.setOrder((AbstractOrderModel)order);
        couponRedemption.setUser(order.getUser());
        getModelService().save(couponRedemption);
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


    @Required
    public void setRedemptionStrategyMap(Map<String, CouponRedemptionStrategy> redemptionStrategyMap)
    {
        this.redemptionStrategyMap = redemptionStrategyMap;
    }


    protected Map<String, CouponRedemptionStrategy> getRedemptionStrategyMap()
    {
        return this.redemptionStrategyMap;
    }


    protected List<FindCouponStrategy> getFindCouponStrategiesList()
    {
        return this.findCouponStrategiesList;
    }


    @Required
    public void setFindCouponStrategiesList(List<FindCouponStrategy> findCouponStrategiesList)
    {
        this.findCouponStrategiesList = findCouponStrategiesList;
    }
}
