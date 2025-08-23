package de.hybris.platform.droolsruleengineservices.versioning.impl;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.versioning.impl.AbstractHistoricalRuleContentProvider;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.Map;

public class DroolsEngineHistoricalRuleContentProvider extends AbstractHistoricalRuleContentProvider
{
    public void copyOriginalValuesIntoHistoricalVersion(AbstractRuleEngineRuleModel ruleModel, AbstractRuleEngineRuleModel historicalRuleModel, InterceptorContext ctx)
    {
        if(historicalRuleModel instanceof DroolsRuleModel)
        {
            DroolsRuleModel historicalDroolModel = (DroolsRuleModel)historicalRuleModel;
            historicalDroolModel.setGlobals((Map)getOriginal(ruleModel, ctx, "globals"));
            historicalDroolModel.setKieBase((DroolsKIEBaseModel)getOriginal(ruleModel, ctx, "kieBase"));
            historicalDroolModel.setRulePackage((String)getOriginal(ruleModel, ctx, "rulePackage"));
        }
    }
}
