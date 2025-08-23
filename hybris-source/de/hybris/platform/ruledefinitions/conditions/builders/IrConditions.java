package de.hybris.platform.ruledefinitions.conditions.builders;

import de.hybris.platform.ruleengineservices.compiler.RuleIrEmptyCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrFalseCondition;

public class IrConditions
{
    public static RuleIrFalseCondition newIrRuleFalseCondition()
    {
        return new RuleIrFalseCondition();
    }


    public static RuleIrEmptyCondition empty()
    {
        return new RuleIrEmptyCondition();
    }
}
