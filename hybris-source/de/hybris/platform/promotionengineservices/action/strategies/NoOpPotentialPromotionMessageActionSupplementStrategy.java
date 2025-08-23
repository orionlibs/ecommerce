package de.hybris.platform.promotionengineservices.action.strategies;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.ActionSupplementStrategy;
import org.apache.commons.collections.MapUtils;

public class NoOpPotentialPromotionMessageActionSupplementStrategy implements ActionSupplementStrategy
{
    public boolean isActionProperToHandle(AbstractRuleActionRAO actionRao, RuleActionContext context)
    {
        return (actionRao instanceof de.hybris.platform.ruleengineservices.rao.DisplayMessageRAO && MapUtils.isEmpty(context.getParameters()));
    }


    public boolean shouldPerformAction(AbstractRuleActionRAO actionRao, RuleActionContext context)
    {
        Preconditions.checkArgument(isActionProperToHandle(actionRao, context), "The strategy is not proper to handle the action.");
        return true;
    }
}
