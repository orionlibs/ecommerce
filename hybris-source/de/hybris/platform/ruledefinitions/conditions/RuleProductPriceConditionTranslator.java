package de.hybris.platform.ruledefinitions.conditions;

import com.google.common.collect.Lists;
import de.hybris.platform.ruledefinitions.AmountOperator;
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

public class RuleProductPriceConditionTranslator extends AbstractRuleConditionTranslator
{
    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        Map<String, RuleParameterData> conditionParameters = condition.getParameters();
        RuleParameterData operatorParameter = conditionParameters.get("operator");
        RuleParameterData valueParameter = conditionParameters.get("value");
        if(verifyAllPresent(new Object[] {operatorParameter, valueParameter}))
        {
            AmountOperator operator = (AmountOperator)operatorParameter.getValue();
            Map<String, BigDecimal> value = (Map<String, BigDecimal>)valueParameter.getValue();
            if(verifyAllPresent(new Object[] {operator, value}))
            {
                return (RuleIrCondition)getProductPriceConditions(context, operator, value);
            }
        }
        return (RuleIrCondition)IrConditions.empty();
    }


    protected RuleIrGroupCondition getProductPriceConditions(RuleCompilerContext context, AmountOperator operator, Map<String, BigDecimal> value)
    {
        RuleIrGroupCondition irGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.OR).build();
        addProductPriceConditions(context, operator, value, irGroupCondition);
        return irGroupCondition;
    }


    protected void addProductPriceConditions(RuleCompilerContext context, AmountOperator operator, Map<String, BigDecimal> value, RuleIrGroupCondition irGroupCondition)
    {
        String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
        String cartRaoVariable = context.generateVariable(CartRAO.class);
        for(Map.Entry<String, BigDecimal> entry : value.entrySet())
        {
            if(verifyAllPresent(new Object[] {entry.getKey(), entry.getValue()}))
            {
                List<RuleIrCondition> conditions = Lists.newArrayList();
                RuleIrGroupCondition irCurrencyGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).withChildren(conditions).build();
                conditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(cartRaoVariable)
                                .withAttribute("currencyIsoCode")
                                .withOperator(RuleIrAttributeOperator.EQUAL)
                                .withValue(entry.getKey())
                                .build());
                conditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable)
                                .withAttribute("basePrice")
                                .withOperator(RuleIrAttributeOperator.valueOf(operator.name()))
                                .withValue(entry.getValue())
                                .build());
                conditions.add(RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(cartRaoVariable)
                                .withAttribute("entries")
                                .withOperator(RuleIrAttributeOperator.CONTAINS)
                                .withTargetVariable(orderEntryRaoVariable)
                                .build());
                conditions.addAll(getConsumptionSupport().newProductConsumedCondition(context, orderEntryRaoVariable));
                irGroupCondition.getChildren().add(irCurrencyGroupCondition);
            }
        }
    }
}
