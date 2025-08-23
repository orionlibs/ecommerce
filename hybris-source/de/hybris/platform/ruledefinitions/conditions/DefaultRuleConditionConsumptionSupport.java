package de.hybris.platform.ruledefinitions.conditions;

import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeConditionBuilder;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.configuration.Switch;
import de.hybris.platform.ruleengineservices.configuration.SwitchService;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleConditionConsumptionSupport implements RuleConditionConsumptionSupport
{
    private SwitchService switchService;


    public List<RuleIrCondition> newProductConsumedCondition(@Nonnull RuleCompilerContext context, String orderEntryRaoVariable)
    {
        return isConsumptionEnabled() ? (List)List.of(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable)
                        .withAttribute("availableQuantity")
                        .withOperator(RuleIrAttributeOperator.GREATER_THAN_OR_EQUAL)
                        .withValue(Integer.valueOf(1)).build()) : Collections.<RuleIrCondition>emptyList();
    }


    protected boolean isConsumptionEnabled()
    {
        return getSwitchService().isEnabled(Switch.CONSUMPTION);
    }


    protected SwitchService getSwitchService()
    {
        return this.switchService;
    }


    @Required
    public void setSwitchService(SwitchService switchService)
    {
        this.switchService = switchService;
    }
}
