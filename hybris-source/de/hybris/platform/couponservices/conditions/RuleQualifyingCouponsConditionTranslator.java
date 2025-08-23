package de.hybris.platform.couponservices.conditions;

import com.google.common.base.Preconditions;
import de.hybris.platform.couponservices.rao.CouponRAO;
import de.hybris.platform.ruledefinitions.conditions.builders.IrConditions;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

public class RuleQualifyingCouponsConditionTranslator implements RuleConditionTranslator
{
    public static final String COUPON_RAO_COUPON_ID_ATTRIBUTE = "couponId";
    public static final String COUPONS_PARAM = "coupons";


    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        Preconditions.checkNotNull(context, "Rule Compiler Context is not expected to be NULL here");
        Preconditions.checkNotNull(condition, "Rule Condition Data is not expected to be NULL here");
        RuleParameterData couponsParameter = (RuleParameterData)condition.getParameters().get("coupons");
        if(couponsParameter == null)
        {
            return (RuleIrCondition)IrConditions.empty();
        }
        List<String> coupons = (List<String>)couponsParameter.getValue();
        if(CollectionUtils.isEmpty(coupons))
        {
            return (RuleIrCondition)IrConditions.empty();
        }
        RuleIrAttributeCondition irCouponCondition = new RuleIrAttributeCondition();
        String couponRaoVariable = context.generateVariable(CouponRAO.class);
        irCouponCondition.setVariable(couponRaoVariable);
        irCouponCondition.setAttribute("couponId");
        irCouponCondition.setOperator(RuleIrAttributeOperator.IN);
        irCouponCondition.setValue(coupons);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("couponIds", coupons);
        irCouponCondition.setMetadata(metadata);
        return (RuleIrCondition)irCouponCondition;
    }
}
