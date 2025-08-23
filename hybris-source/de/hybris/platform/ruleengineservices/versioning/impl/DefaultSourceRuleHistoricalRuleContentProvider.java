package de.hybris.platform.ruleengineservices.versioning.impl;

import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.versioning.HistoricalRuleContentProvider;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

public class DefaultSourceRuleHistoricalRuleContentProvider implements HistoricalRuleContentProvider
{
    public void copyOriginalValuesIntoHistoricalVersion(SourceRuleModel sourceRule, SourceRuleModel historicalSourceRule, InterceptorContext ctx)
    {
        historicalSourceRule.setUuid(sourceRule.getUuid());
        historicalSourceRule.setCode(sourceRule.getCode());
    }
}
