package de.hybris.platform.ruleengineservices.versioning.impl;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.versioning.impl.AbstractHistoricalRuleContentProvider;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

public class RuleEngineServicesHistoricalRuleContentProvider extends AbstractHistoricalRuleContentProvider
{
    public void copyOriginalValuesIntoHistoricalVersion(AbstractRuleEngineRuleModel ruleModel, AbstractRuleEngineRuleModel historicalRuleModel, InterceptorContext ctx)
    {
        if(historicalRuleModel instanceof DroolsRuleModel)
        {
            DroolsRuleModel historicalDroolModel = (DroolsRuleModel)historicalRuleModel;
            historicalDroolModel.setSourceRule((AbstractRuleModel)getOriginal(ruleModel, ctx, "sourceRule"));
        }
    }
}
