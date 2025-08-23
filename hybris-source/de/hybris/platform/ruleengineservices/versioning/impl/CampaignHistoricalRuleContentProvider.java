package de.hybris.platform.ruleengineservices.versioning.impl;

import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.versioning.HistoricalRuleContentProvider;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelServiceInterceptorContext;
import de.hybris.platform.servicelayer.internal.model.impl.RegisteredElements;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class CampaignHistoricalRuleContentProvider implements HistoricalRuleContentProvider
{
    public void copyOriginalValuesIntoHistoricalVersion(SourceRuleModel sourceRule, SourceRuleModel historicalSourceRule, InterceptorContext ctx)
    {
        if(wasSourceRuleEverPublished((AbstractRuleModel)sourceRule) && ctx instanceof DefaultModelServiceInterceptorContext)
        {
            RegisteredElements registeredElements = ((DefaultModelServiceInterceptorContext)ctx).getInitialElements();
            Objects.requireNonNull(CampaignModel.class);
            Objects.requireNonNull(CampaignModel.class);
            Set<CampaignModel> registeredCampaignSet = (Set<CampaignModel>)registeredElements.unmodifiableSet().stream().filter(CampaignModel.class::isInstance).map(CampaignModel.class::cast).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(registeredCampaignSet))
            {
                Set<CampaignModel> campaignSet = (Set<CampaignModel>)registeredCampaignSet.stream().filter(c -> c.getSourceRules().contains(sourceRule)).collect(Collectors.toSet());
                if(CollectionUtils.isNotEmpty(campaignSet))
                {
                    campaignSet.forEach(c -> substituteAssociatedSourceRule(c, sourceRule, historicalSourceRule));
                    campaignSet.forEach(c -> ctx.registerElementFor(c, PersistenceOperation.SAVE));
                }
            }
        }
    }


    protected void substituteAssociatedSourceRule(CampaignModel campaign, SourceRuleModel ruleToRemove, SourceRuleModel ruleToAdd)
    {
        Set<SourceRuleModel> sourceRules = campaign.getSourceRules();
        sourceRules.remove(ruleToRemove);
        sourceRules.add(ruleToAdd);
    }


    protected boolean wasSourceRuleEverPublished(AbstractRuleModel rule)
    {
        return (rule instanceof SourceRuleModel && !rule.getStatus().equals(RuleStatus.UNPUBLISHED));
    }
}
