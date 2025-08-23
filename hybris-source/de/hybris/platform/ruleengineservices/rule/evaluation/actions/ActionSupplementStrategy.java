package de.hybris.platform.ruleengineservices.rule.evaluation.actions;

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;

public interface ActionSupplementStrategy
{
    public static final String UUID_SUFFIX = "_uuid";


    boolean isActionProperToHandle(AbstractRuleActionRAO paramAbstractRuleActionRAO, RuleActionContext paramRuleActionContext);


    default void postProcessAction(AbstractRuleActionRAO actionRao, RuleActionContext context)
    {
    }


    boolean shouldPerformAction(AbstractRuleActionRAO paramAbstractRuleActionRAO, RuleActionContext paramRuleActionContext);
}
