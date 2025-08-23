package de.hybris.platform.ruleengine.util;

import java.util.Collection;

public interface EngineRulesRepository
{
    <T extends de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel> boolean checkEngineRuleDeployedForModule(T paramT, String paramString);


    <T extends de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel> Collection<T> getDeployedEngineRulesForModule(String paramString);


    long countDeployedEngineRulesForModule(String paramString);
}
