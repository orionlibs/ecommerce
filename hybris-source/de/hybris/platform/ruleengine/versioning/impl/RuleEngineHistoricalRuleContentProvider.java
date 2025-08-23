package de.hybris.platform.ruleengine.versioning.impl;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.Date;
import java.util.List;

public class RuleEngineHistoricalRuleContentProvider extends AbstractHistoricalRuleContentProvider
{
    public void copyOriginalValuesIntoHistoricalVersion(AbstractRuleEngineRuleModel ruleModel, AbstractRuleEngineRuleModel historicalRuleModel, InterceptorContext ctx)
    {
        historicalRuleModel.setActive((Boolean)getOriginal(ruleModel, ctx, "active"));
        historicalRuleModel.setUuid((String)getOriginal(ruleModel, ctx, "uuid"));
        historicalRuleModel.setChecksum((String)getOriginal(ruleModel, ctx, "checksum"));
        historicalRuleModel.setRuleContent((String)getOriginal(ruleModel, ctx, "ruleContent"));
        historicalRuleModel.setVersion((Long)getOriginal(ruleModel, ctx, "version"));
        historicalRuleModel.setModifiedtime((Date)getOriginal(ruleModel, ctx, "modifiedtime"));
        historicalRuleModel.setCreationtime((Date)getOriginal(ruleModel, ctx, "creationtime"));
        historicalRuleModel
                        .setComments((List)getOriginal(ruleModel, ctx, "comments"));
        historicalRuleModel
                        .setRuleParameters((String)getOriginal(ruleModel, ctx, "ruleParameters"));
        historicalRuleModel.setMessageFired((String)getOriginal(ruleModel, ctx, "messageFired"));
    }
}
