package de.hybris.platform.couponservices.order.hooks;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.couponservices.couponcodegeneration.CouponCodeGenerationException;
import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.dao.RuleBasedCouponActionDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.model.RuleBasedAddCouponActionModel;
import de.hybris.platform.couponservices.services.CouponCodeGenerationService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class GiveAwayMultiCodeCouponGenerationHook implements CommercePlaceOrderMethodHook
{
    private CouponDao couponDao;
    private RuleBasedCouponActionDao ruleBasedCouponActionDao;
    private CouponCodeGenerationService couponCodeGenerationService;
    private ModelService modelService;
    private static final Logger LOG = LoggerFactory.getLogger(GiveAwayMultiCodeCouponGenerationHook.class);


    public void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("order", result.getOrder());
        List<RuleBasedAddCouponActionModel> couponActionList = getRuleBasedCouponActionDao().findRuleBasedCouponActionByOrder(result.getOrder());
        for(RuleBasedAddCouponActionModel couponAction : couponActionList)
        {
            if(StringUtils.isNotEmpty(couponAction.getCouponId()))
            {
                generateGiveAwayMultiCodeCoupon(couponAction);
                continue;
            }
            LOG.warn("Cannot generate Give Away Multi Code Coupon as coupon id is empty");
        }
    }


    protected void generateGiveAwayMultiCodeCoupon(RuleBasedAddCouponActionModel couponAction)
    {
        try
        {
            AbstractCouponModel coupon = getCouponDao().findCouponById(couponAction.getCouponId());
            if(coupon instanceof MultiCodeCouponModel)
            {
                generateGiveAwayMultiCodeCoupon((MultiCodeCouponModel)coupon, couponAction);
            }
        }
        catch(ModelNotFoundException | de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException ex)
        {
            LOG.error("Cannot generate Give Away Multi Code Coupon for coupon id: {}, not able to find the coupon object", couponAction
                            .getCouponId(), ex);
            setMultiCodeCouponToAction(couponAction, "");
        }
    }


    protected void generateGiveAwayMultiCodeCoupon(MultiCodeCouponModel coupon, RuleBasedAddCouponActionModel couponAction)
    {
        try
        {
            String multiCodeCouponCode = getCouponCodeGenerationService().generateCouponCode(coupon);
            setMultiCodeCouponToAction(couponAction, multiCodeCouponCode);
            getModelService().save(coupon);
        }
        catch(CouponCodeGenerationException ex)
        {
            LOG.error("Cannot generate Give Away Multi Code Coupon due to Coupon Code Generation Exception", (Throwable)ex);
            setMultiCodeCouponToAction(couponAction, "");
        }
    }


    protected void setMultiCodeCouponToAction(RuleBasedAddCouponActionModel couponAction, String multiCodeCouponCode)
    {
        couponAction.setCouponCode(multiCodeCouponCode);
        getModelService().save(couponAction);
    }


    public void afterPlaceOrder(CommerceCheckoutParameter parameter, CommerceOrderResult orderModel) throws InvalidCartException
    {
    }


    public void beforePlaceOrder(CommerceCheckoutParameter parameter) throws InvalidCartException
    {
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


    protected CouponCodeGenerationService getCouponCodeGenerationService()
    {
        return this.couponCodeGenerationService;
    }


    @Required
    public void setCouponCodeGenerationService(CouponCodeGenerationService couponCodeGenerationService)
    {
        this.couponCodeGenerationService = couponCodeGenerationService;
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


    protected RuleBasedCouponActionDao getRuleBasedCouponActionDao()
    {
        return this.ruleBasedCouponActionDao;
    }


    @Required
    public void setRuleBasedCouponActionDao(RuleBasedCouponActionDao ruleBasedCouponActionDao)
    {
        this.ruleBasedCouponActionDao = ruleBasedCouponActionDao;
    }
}
