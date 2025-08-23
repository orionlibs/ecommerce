package de.hybris.platform.ruleengine.versioning.impl;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.versioning.HistoricalRuleContentProvider;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Objects;

public abstract class AbstractHistoricalRuleContentProvider implements HistoricalRuleContentProvider
{
    protected <T> T getOriginal(AbstractRuleEngineRuleModel droolsRule, InterceptorContext context, String attributeQualifier)
    {
        if(context.isModified(droolsRule, attributeQualifier))
        {
            ItemModelContext modelContext = ModelContextUtils.getItemModelContext((AbstractItemModel)droolsRule);
            return (T)modelContext.getOriginalValue(attributeQualifier);
        }
        ModelService modelService = Objects.<ModelService>requireNonNull(context.getModelService());
        return (T)modelService.getAttributeValue(droolsRule, attributeQualifier);
    }
}
