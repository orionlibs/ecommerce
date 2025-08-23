package de.hybris.platform.ruleengine.init;

import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;

public interface RuleEngineBootstrap<SERVICES, CONTAINER, MODULE extends de.hybris.platform.ruleengine.model.AbstractRulesModuleModel>
{
    SERVICES getEngineServices();


    RuleEngineActionResult startup(String paramString);


    void warmUpRuleEngineContainer(MODULE paramMODULE, CONTAINER paramCONTAINER);


    void activateNewRuleEngineContainer(CONTAINER paramCONTAINER, KIEModuleCacheBuilder paramKIEModuleCacheBuilder, RuleEngineActionResult paramRuleEngineActionResult, MODULE paramMODULE, String paramString);
}
