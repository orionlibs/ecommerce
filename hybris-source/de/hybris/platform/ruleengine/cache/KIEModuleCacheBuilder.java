package de.hybris.platform.ruleengine.cache;

public interface KIEModuleCacheBuilder
{
    <T extends de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel> void processRule(T paramT);
}
