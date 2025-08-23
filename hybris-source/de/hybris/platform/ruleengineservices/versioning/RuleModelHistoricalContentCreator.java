package de.hybris.platform.ruleengineservices.versioning;

import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

public interface RuleModelHistoricalContentCreator
{
    void createHistoricalVersion(SourceRuleModel paramSourceRuleModel, InterceptorContext paramInterceptorContext) throws InterceptorException;
}
