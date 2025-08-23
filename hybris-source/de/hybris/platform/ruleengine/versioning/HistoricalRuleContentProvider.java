package de.hybris.platform.ruleengine.versioning;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

public interface HistoricalRuleContentProvider
{
    void copyOriginalValuesIntoHistoricalVersion(AbstractRuleEngineRuleModel paramAbstractRuleEngineRuleModel1, AbstractRuleEngineRuleModel paramAbstractRuleEngineRuleModel2, InterceptorContext paramInterceptorContext);
}
