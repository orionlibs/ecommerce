package de.hybris.platform.ruleengine.versioning;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

@FunctionalInterface
public interface RuleModelHistoricalContentCreator
{
    void createHistoricalVersion(AbstractRuleEngineRuleModel paramAbstractRuleEngineRuleModel, InterceptorContext paramInterceptorContext);
}
