package de.hybris.platform.ruleengine.impl;

import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import org.kie.api.runtime.KieContainer;

public interface KieContainerListener
{
    void onSuccess(KieContainer paramKieContainer, KIEModuleCacheBuilder paramKIEModuleCacheBuilder);


    void onFailure(RuleEngineActionResult paramRuleEngineActionResult);
}
