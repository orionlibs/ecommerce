package de.hybris.platform.ruleengineservices.versioning;

import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

public interface HistoricalRuleContentProvider
{
    void copyOriginalValuesIntoHistoricalVersion(SourceRuleModel paramSourceRuleModel1, SourceRuleModel paramSourceRuleModel2, InterceptorContext paramInterceptorContext);
}
