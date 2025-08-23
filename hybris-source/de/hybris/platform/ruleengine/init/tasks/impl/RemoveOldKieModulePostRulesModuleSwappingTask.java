package de.hybris.platform.ruleengine.init.tasks.impl;

import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.init.RuleEngineKieModuleSwapper;
import de.hybris.platform.ruleengine.init.tasks.PostRulesModuleSwappingTask;
import org.springframework.beans.factory.annotation.Required;

public class RemoveOldKieModulePostRulesModuleSwappingTask implements PostRulesModuleSwappingTask
{
    private RuleEngineKieModuleSwapper ruleEngineKieModuleSwapper;


    public boolean execute(RuleEngineActionResult result)
    {
        boolean removed = false;
        if(!result.isActionFailed())
        {
            removed = getRuleEngineKieModuleSwapper().removeOldKieModuleIfPresent(result);
        }
        return removed;
    }


    protected RuleEngineKieModuleSwapper getRuleEngineKieModuleSwapper()
    {
        return this.ruleEngineKieModuleSwapper;
    }


    @Required
    public void setRuleEngineKieModuleSwapper(RuleEngineKieModuleSwapper ruleEngineKieModuleSwapper)
    {
        this.ruleEngineKieModuleSwapper = ruleEngineKieModuleSwapper;
    }
}
