package de.hybris.platform.couponservices.dao;

import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.CustomerCouponForPromotionSourceRuleModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.model.SingleCodeCouponModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.List;

public interface CouponDao extends Dao
{
    AbstractCouponModel findCouponById(String paramString);


    SingleCodeCouponModel findSingleCodeCouponById(String paramString);


    MultiCodeCouponModel findMultiCodeCouponById(String paramString);


    List<MultiCodeCouponModel> findMultiCodeCouponsByCodeConfiguration(CodeGenerationConfigurationModel paramCodeGenerationConfigurationModel);


    List<CustomerCouponForPromotionSourceRuleModel> findAllCouponForSourceRules(PromotionSourceRuleModel paramPromotionSourceRuleModel);


    List<CustomerCouponForPromotionSourceRuleModel> findAllCouponForSourceRules(PromotionSourceRuleModel paramPromotionSourceRuleModel, String paramString);


    List<PromotionSourceRuleModel> findPromotionSourceRuleByCouponCode(String paramString);
}
