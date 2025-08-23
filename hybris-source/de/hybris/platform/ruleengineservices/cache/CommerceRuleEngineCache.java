package de.hybris.platform.ruleengineservices.cache;

import de.hybris.platform.ruleengine.cache.RuleEngineCache;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import java.util.Collection;

public interface CommerceRuleEngineCache extends RuleEngineCache
{
    Collection<Object> getCachedFacts(DroolsKIEBaseModel paramDroolsKIEBaseModel);
}
