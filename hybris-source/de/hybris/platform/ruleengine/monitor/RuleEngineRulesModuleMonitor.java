package de.hybris.platform.ruleengine.monitor;

public interface RuleEngineRulesModuleMonitor<T extends de.hybris.platform.ruleengine.model.AbstractRulesModuleModel>
{
    boolean isRulesModuleDeployed(T paramT);
}
