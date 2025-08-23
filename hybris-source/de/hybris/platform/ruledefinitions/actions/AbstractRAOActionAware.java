package de.hybris.platform.ruledefinitions.actions;

import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleExecutableAction;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.RAOAction;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractRAOActionAware implements RuleExecutableAction
{
    private RAOAction raoAction;


    public void executeAction(RuleActionContext context, Map<String, Object> parameters)
    {
        context.setParameters(parameters);
        getRaoAction().performAction(context);
    }


    protected RAOAction getRaoAction()
    {
        return this.raoAction;
    }


    @Required
    public void setRaoAction(RAOAction raoAction)
    {
        this.raoAction = raoAction;
    }
}
