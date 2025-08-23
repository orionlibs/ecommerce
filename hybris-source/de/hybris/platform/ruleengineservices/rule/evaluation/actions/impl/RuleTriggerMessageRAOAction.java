package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import com.google.common.collect.Maps;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DisplayMessageRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRuleExecutableSupport;
import java.util.Map;

public class RuleTriggerMessageRAOAction extends AbstractRuleExecutableSupport
{
    public boolean performActionInternal(RuleActionContext context)
    {
        DisplayMessageRAO message = new DisplayMessageRAO();
        message.setParameters(Maps.newHashMap());
        if(shouldPerformAction((AbstractRuleActionRAO)message, context))
        {
            RuleEngineResultRAO result = context.getRuleEngineResultRao();
            CartRAO cartRAO = context.getCartRao();
            getRaoUtils().addAction((AbstractActionedRAO)cartRAO, (AbstractRuleActionRAO)message);
            result.getActions().add(message);
            setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)message});
            context.scheduleForUpdate(new Object[] {cartRAO, result});
            context.insertFacts(new Object[] {message});
            postProcessAction((AbstractRuleActionRAO)message, context);
            return true;
        }
        return false;
    }


    protected void validateParameters(Map<String, Object> parameters)
    {
    }
}
