package de.hybris.platform.ruledefinitions.conditions;

import com.google.common.collect.Lists;
import de.hybris.platform.ruledefinitions.conditions.builders.IrConditions;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeRelConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrGroupConditionBuilder;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class RuleOrderThresholdPerfectPartnerConditionTranslator extends AbstractRuleConditionTranslator
{
    public static final String PARTNER_PRODUCT_PARAM = "product";
    public static final String CART_THRESHOLD_PARAM = "cart_threshold";
    public static final String IS_DISCOUNTED_PRICE_INCLUDED_PARAM = "is_discounted_price_included";
    private String orderThresholdConditionAttribute;


    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        Map<String, RuleParameterData> conditionParameters = condition.getParameters();
        RuleParameterData partnerProductParam = conditionParameters.get("product");
        RuleParameterData cartThresholdParam = conditionParameters.get("cart_threshold");
        RuleParameterData isPriceIncludedParam = conditionParameters.get("is_discounted_price_included");
        if(verifyAllPresent(new Object[] {partnerProductParam, cartThresholdParam, isPriceIncludedParam}))
        {
            String partnerProduct = (String)partnerProductParam.getValue();
            Map<String, BigDecimal> cartThreshold = (Map<String, BigDecimal>)cartThresholdParam.getValue();
            Boolean isPriceIncluded = (Boolean)isPriceIncludedParam.getValue();
            if(verifyAllPresent(new Object[] {partnerProduct, cartThreshold, isPriceIncluded}))
            {
                return getConditions(context, partnerProduct, cartThreshold);
            }
        }
        return (RuleIrCondition)IrConditions.empty();
    }


    protected RuleIrCondition getConditions(RuleCompilerContext context, String partnerProduct, Map<String, BigDecimal> cartThreshold)
    {
        RuleIrGroupCondition irGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.OR).build();
        String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
        String cartRaoVariable = context.generateVariable(CartRAO.class);
        for(Map.Entry<String, BigDecimal> entry : cartThreshold.entrySet())
        {
            if(verifyAllPresent(new Object[] {entry.getKey(), entry.getValue()}))
            {
                List<RuleIrCondition> conditions = Lists.newArrayList();
                RuleIrGroupCondition irCurrencyGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).withChildren(conditions).build();
                conditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable)
                                .withAttribute("productCode")
                                .withOperator(RuleIrAttributeOperator.EQUAL)
                                .withValue(partnerProduct)
                                .build());
                conditions.add(RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(cartRaoVariable)
                                .withAttribute("entries")
                                .withOperator(RuleIrAttributeOperator.CONTAINS)
                                .withTargetVariable(orderEntryRaoVariable)
                                .build());
                conditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(cartRaoVariable).withAttribute("currencyIsoCode")
                                .withOperator(RuleIrAttributeOperator.EQUAL).withValue(entry.getKey())
                                .build());
                conditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(cartRaoVariable)
                                .withAttribute(getOrderThresholdConditionAttribute())
                                .withOperator(RuleIrAttributeOperator.GREATER_THAN_OR_EQUAL)
                                .withValue(entry.getValue())
                                .build());
                conditions.addAll(getConsumptionSupport().newProductConsumedCondition(context, orderEntryRaoVariable));
                irGroupCondition.getChildren().add(irCurrencyGroupCondition);
            }
        }
        return (RuleIrCondition)irGroupCondition;
    }


    protected String getOrderThresholdConditionAttribute()
    {
        return this.orderThresholdConditionAttribute;
    }


    @Required
    public void setOrderThresholdConditionAttribute(String orderThresholdConditionAttribute)
    {
        this.orderThresholdConditionAttribute = orderThresholdConditionAttribute;
    }
}
