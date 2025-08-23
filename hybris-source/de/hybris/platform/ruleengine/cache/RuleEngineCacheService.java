package de.hybris.platform.ruleengine.cache;

import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;

public interface RuleEngineCacheService
{
    KIEModuleCacheBuilder createKIEModuleCacheBuilder(DroolsKIEModuleModel paramDroolsKIEModuleModel);


    void addToCache(KIEModuleCacheBuilder paramKIEModuleCacheBuilder);


    void provideCachedEntities(RuleEvaluationContext paramRuleEvaluationContext);
}
