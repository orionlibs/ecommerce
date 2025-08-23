package de.hybris.platform.droolsruleengineservices.compiler.impl;

import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleGeneratorContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConditionsDroolsRuleValueFormatter extends DefaultDroolsRuleValueFormatter
{
    private final Map<RuleIrAttributeOperator, Function<RuleIrAttributeCondition, Supplier<Map<String, DefaultDroolsRuleValueFormatter.DroolsRuleValueFormatterHelper>>>> formatterSuppliers = (Map<RuleIrAttributeOperator, Function<RuleIrAttributeCondition, Supplier<Map<String, DefaultDroolsRuleValueFormatter.DroolsRuleValueFormatterHelper>>>>)new Object(
                    this, RuleIrAttributeOperator.class);


    public String formatValue(DroolsRuleGeneratorContext context, Object object)
    {
        if(object instanceof RuleIrAttributeCondition)
        {
            RuleIrAttributeCondition attribute = (RuleIrAttributeCondition)object;
            return getFormatterSuppliers().containsKey(attribute.getOperator()) ?
                            formatValue(context, attribute.getValue(), ((Function<RuleIrAttributeCondition, Supplier>)getFormatterSuppliers().get(attribute.getOperator())).apply(attribute)) :
                            formatValue(context, attribute.getValue());
        }
        return super.formatValue(context, object);
    }


    protected Map<RuleIrAttributeOperator, Function<RuleIrAttributeCondition, Supplier<Map<String, DefaultDroolsRuleValueFormatter.DroolsRuleValueFormatterHelper>>>> getFormatterSuppliers()
    {
        return this.formatterSuppliers;
    }
}
