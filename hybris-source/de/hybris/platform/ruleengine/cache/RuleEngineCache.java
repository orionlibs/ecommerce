package de.hybris.platform.ruleengine.cache;

import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import java.util.Map;

public interface RuleEngineCache
{
    KIEModuleCacheBuilder createKIEModuleCacheBuilder(DroolsKIEModuleModel paramDroolsKIEModuleModel);


    void addKIEModuleCache(KIEModuleCacheBuilder paramKIEModuleCacheBuilder);


    Map<String, Object> getGlobalsForKIEBase(DroolsKIEBaseModel paramDroolsKIEBaseModel);
}
