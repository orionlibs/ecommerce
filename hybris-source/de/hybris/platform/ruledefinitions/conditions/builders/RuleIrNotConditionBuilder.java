package de.hybris.platform.ruledefinitions.conditions.builders;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrLocalVariablesContainer;
import de.hybris.platform.ruleengineservices.compiler.RuleIrNotCondition;
import java.util.List;

public class RuleIrNotConditionBuilder
{
    private static RuleIrNotConditionBuilder self = new RuleIrNotConditionBuilder();
    private RuleIrNotCondition condition;


    public static RuleIrNotConditionBuilder newNotCondition()
    {
        self.condition = new RuleIrNotCondition();
        self.condition.setChildren(Lists.newArrayList());
        return self;
    }


    public RuleIrNotConditionBuilder withVariablesContainer(RuleIrLocalVariablesContainer container)
    {
        self.condition.setVariablesContainer(container);
        return self;
    }


    public RuleIrNotConditionBuilder withChildren(List<RuleIrCondition> leafConditions)
    {
        self.condition.setChildren(leafConditions);
        return self;
    }


    public RuleIrNotCondition build()
    {
        return this.condition;
    }
}
