package de.hybris.platform.couponservices.rule.strategies.impl.mappers;

import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class CouponRuleParameterValueMapper implements RuleParameterValueMapper<AbstractCouponModel>
{
    private CouponDao couponDao;


    public AbstractCouponModel fromString(String value)
    {
        ServicesUtil.validateParameterNotNull(value, "String value cannot be null");
        try
        {
            return getCouponById(value);
        }
        catch(ModelNotFoundException ex)
        {
            throw new RuleParameterValueMapperException("Cannot find coupon with the couponId: " + value, ex);
        }
    }


    protected AbstractCouponModel getCouponById(String couponId)
    {
        return getCouponDao().findCouponById(couponId);
    }


    public String toString(AbstractCouponModel value)
    {
        ServicesUtil.validateParameterNotNull(value, "CouponModel cannot be null");
        return value.getCouponId();
    }


    protected CouponDao getCouponDao()
    {
        return this.couponDao;
    }


    @Required
    public void setCouponDao(CouponDao couponDao)
    {
        this.couponDao = couponDao;
    }
}
