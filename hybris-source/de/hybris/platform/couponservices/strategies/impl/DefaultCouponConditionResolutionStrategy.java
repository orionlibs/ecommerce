package de.hybris.platform.couponservices.strategies.impl;

import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.model.CustomerCouponForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.compiler.strategies.ConditionResolutionStrategy;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class DefaultCouponConditionResolutionStrategy implements ConditionResolutionStrategy
{
    private ModelService modelService;
    private CouponDao couponDao;


    public void getAndStoreParameterValues(RuleConditionData condition, PromotionSourceRuleModel rule, RuleBasedPromotionModel promotion)
    {
        RuleParameterData couponRuleParams = (RuleParameterData)condition.getParameters().get("coupons");
        if(couponRuleParams != null)
        {
            List<String> couponCodes = (List<String>)couponRuleParams.getValue();
            if(CollectionUtils.isNotEmpty(couponCodes))
            {
                couponCodes.forEach(code -> {
                    CustomerCouponForPromotionSourceRuleModel cusCouponForRule = (CustomerCouponForPromotionSourceRuleModel)getModelService().create(CustomerCouponForPromotionSourceRuleModel.class);
                    cusCouponForRule.setCustomerCouponCode(code);
                    cusCouponForRule.setRule(rule);
                    cusCouponForRule.setPromotion(promotion);
                    getModelService().save(cusCouponForRule);
                });
            }
        }
    }


    public void cleanStoredParameterValues(RuleCompilerContext context)
    {
        getModelService().removeAll(
                        getCouponDao().findAllCouponForSourceRules((PromotionSourceRuleModel)context.getRule(), context
                                        .getModuleName()));
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected CouponDao getCouponDao()
    {
        return this.couponDao;
    }


    public void setCouponDao(CouponDao couponDao)
    {
        this.couponDao = couponDao;
    }
}
