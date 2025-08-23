package de.hybris.platform.ruledefinitions.conditions;

import de.hybris.platform.ruledefinitions.AmountOperator;
import de.hybris.platform.ruledefinitions.conditions.builders.IrConditions;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrGroupConditionBuilder;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class RuleCartTotalConditionTranslator extends AbstractRuleConditionTranslator
{
    private String cartThresholdConditionAttribute;


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
                return (RuleIrCondition)getCartTotalConditions(context, operator, value);
            }
        }
        return (RuleIrCondition)IrConditions.empty();
    }


    protected RuleIrGroupCondition getCartTotalConditions(RuleCompilerContext context, AmountOperator operator, Map<String, BigDecimal> value)
    {
        RuleIrGroupCondition irCartTotalCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.OR).build();
        addCartTotalConditions(context, operator, value, irCartTotalCondition);
        return irCartTotalCondition;
    }


    protected void addCartTotalConditions(RuleCompilerContext context, AmountOperator operator, Map<String, BigDecimal> value, RuleIrGroupCondition irCartTotalCondition)
    {
        String cartRaoVariable = context.generateVariable(CartRAO.class);
        for(Map.Entry<String, BigDecimal> entry : value.entrySet())
        {
            if(verifyAllPresent(new Object[] {entry.getKey(), entry.getValue()}))
            {
                RuleIrGroupCondition irCurrencyGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
                List<RuleIrCondition> ruleIrConditions = irCurrencyGroupCondition.getChildren();
                ruleIrConditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(cartRaoVariable)
                                .withAttribute("currencyIsoCode")
                                .withOperator(RuleIrAttributeOperator.EQUAL)
                                .withValue(entry.getKey())
                                .build());
                ruleIrConditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(cartRaoVariable)
                                .withAttribute(getCartThresholdConditionAttribute())
                                .withOperator(RuleIrAttributeOperator.valueOf(operator.name()))
                                .withValue(entry.getValue())
                                .build());
                irCartTotalCondition.getChildren().add(irCurrencyGroupCondition);
            }
        }
    }


    protected String getCartThresholdConditionAttribute()
    {
        return this.cartThresholdConditionAttribute;
    }


    @Required
    public void setCartThresholdConditionAttribute(String cartThresholdConditionAttribute)
    {
        this.cartThresholdConditionAttribute = cartThresholdConditionAttribute;
    }
}
