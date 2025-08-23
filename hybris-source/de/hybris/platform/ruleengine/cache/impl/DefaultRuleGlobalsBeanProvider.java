package de.hybris.platform.ruleengine.cache.impl;

import de.hybris.platform.ruleengine.cache.RuleGlobalsBeanProvider;
import de.hybris.platform.ruleengine.infrastructure.GetRuleEngineGlobalByName;

public class DefaultRuleGlobalsBeanProvider implements RuleGlobalsBeanProvider
{
    @GetRuleEngineGlobalByName
    public Object getRuleGlobals(String key)
    {
        return null;
    }
}
