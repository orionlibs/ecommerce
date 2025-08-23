package de.hybris.platform.couponservices.strategies.impl;

import de.hybris.platform.couponservices.CouponServiceException;
import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.strategies.FindCouponStrategy;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFindCouponStrategy implements FindCouponStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFindCouponStrategy.class);
    private CouponDao couponDao;
    private ConfigurationService configurationService;


    public Optional<AbstractCouponModel> findCouponForCouponCode(String couponCode)
    {
        return getCouponByCode(couponCode);
    }


    public Optional<AbstractCouponModel> findValidatedCouponForCouponCode(String couponCode)
    {
        Optional<AbstractCouponModel> couponModel = getCouponByCode(couponCode);
        return couponModel.isPresent() ? couponValidation(couponModel.get()) : Optional.<AbstractCouponModel>empty();
    }


    protected Optional<AbstractCouponModel> getCouponByCode(String couponCode)
    {
        String couponId = getCouponId(couponCode);
        if(StringUtils.isNotEmpty(couponId))
        {
            try
            {
                AbstractCouponModel coupon = getCouponDao().findCouponById(couponId);
                return Optional.of(coupon);
            }
            catch(ModelNotFoundException | de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException ex)
            {
                LOG.debug(ex.getMessage(), (Throwable)ex);
            }
        }
        return Optional.empty();
    }


    protected abstract String getCouponId(String paramString);


    protected Optional<AbstractCouponModel> couponValidation(AbstractCouponModel coupon)
    {
        if(isActive(coupon) && isWithinDateRange(coupon) && isCouponUsedInPromotion(coupon))
        {
            return Optional.of(coupon);
        }
        throw new CouponServiceException("coupon.invalid.code.provided");
    }


    protected boolean isActive(AbstractCouponModel coupon)
    {
        return BooleanUtils.isTrue(coupon.getActive());
    }


    protected boolean isWithinDateRange(AbstractCouponModel coupon)
    {
        Date currentDate = new Date();
        return (isStartDateBefore(currentDate, coupon.getStartDate()) && isEndDateAfter(currentDate, coupon.getEndDate()));
    }


    protected boolean isCouponUsedInPromotion(AbstractCouponModel coupon)
    {
        if(!getConfigurationService().getConfiguration().getBoolean("couponservices.coupon.redemption.validation", false))
        {
            return true;
        }
        List<PromotionSourceRuleModel> bindPromotion = getCouponDao().findPromotionSourceRuleByCouponCode(coupon
                        .getCouponId());
        return CollectionUtils.isNotEmpty(bindPromotion);
    }


    protected boolean isStartDateBefore(Date date, Date startDate)
    {
        return (startDate == null || date.getTime() >= startDate.getTime());
    }


    protected boolean isEndDateAfter(Date date, Date endDate)
    {
        return (endDate == null || date.getTime() <= endDate.getTime());
    }


    protected CouponDao getCouponDao()
    {
        return this.couponDao;
    }


    public void setCouponDao(CouponDao couponDao)
    {
        this.couponDao = couponDao;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
