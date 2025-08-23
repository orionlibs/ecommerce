package de.hybris.platform.ruleengineservices.versioning.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelServiceInterceptorContext;
import de.hybris.platform.servicelayer.internal.model.impl.RegisteredElements;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class CampaignSourceRuleModelHistoricalContentCreator extends SourceRuleModelHistoricalContentCreator
{
    protected boolean associatedTypesChanged(AbstractRuleModel ruleModel, InterceptorContext ctx)
    {
        boolean changed = super.associatedTypesChanged(ruleModel, ctx);
        if(!changed)
        {
            changed = associatedCampaignsChanged(ruleModel, ctx);
        }
        return changed;
    }


    protected boolean associatedCampaignsChanged(AbstractRuleModel rule, InterceptorContext ctx)
    {
        if(wasSourceRuleEverPublished(rule) && ctx instanceof DefaultModelServiceInterceptorContext)
        {
            SourceRuleModel sourceRule = (SourceRuleModel)rule;
            RegisteredElements registeredElements = ((DefaultModelServiceInterceptorContext)ctx).getInitialElements();
            Objects.requireNonNull(CampaignModel.class);
            Objects.requireNonNull(CampaignModel.class);
            Set<CampaignModel> registeredCampaignSet = (Set<CampaignModel>)registeredElements.unmodifiableSet().stream().filter(CampaignModel.class::isInstance).map(CampaignModel.class::cast).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(registeredCampaignSet))
            {
                Set<CampaignModel> associatedCampaignSet = (Set<CampaignModel>)registeredCampaignSet.stream().filter(c -> c.getSourceRules().contains(sourceRule)).collect(Collectors.toSet());
                ItemModelContext modelContext = ModelContextUtils.getItemModelContext((AbstractItemModel)sourceRule);
                Set<CampaignModel> origCampaigns = (Set<CampaignModel>)modelContext.getOriginalValue("campaigns");
                return !Sets.symmetricDifference(associatedCampaignSet, origCampaigns).isEmpty();
            }
        }
        return false;
    }


    protected boolean wasSourceRuleEverPublished(AbstractRuleModel rule)
    {
        return (rule instanceof SourceRuleModel && !rule.getStatus().equals(RuleStatus.UNPUBLISHED));
    }
}
