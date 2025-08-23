package de.hybris.platform.ruleengineservices.rule.interceptors;

import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class CampaignRuleValidateInterceptor implements ValidateInterceptor<CampaignModel>
{
    private static final String PUBLISHED_RULES_ERROR_MESSAGE = "exception.campaignrulevalidateinterceptor";
    private L10NService l10NService;


    public void onValidate(CampaignModel campaign, InterceptorContext ctx) throws InterceptorException
    {
        if(!ctx.isNew(campaign))
        {
            Set<SourceRuleModel> sourceRules = campaign.getSourceRules();
            Set<SourceRuleModel> frozenAssociatedSourceRules = getFrozenAssociatedSourceRules(campaign);
            if(CollectionUtils.isNotEmpty(frozenAssociatedSourceRules) && !sourceRules.containsAll(frozenAssociatedSourceRules))
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.campaignrulevalidateinterceptor", new Object[] {RuleStatus.UNPUBLISHED}), this);
            }
        }
    }


    protected Set<SourceRuleModel> getFrozenAssociatedSourceRules(CampaignModel campaign)
    {
        ItemModelContext modelContext = ModelContextUtils.getItemModelContext((AbstractItemModel)campaign);
        Set<SourceRuleModel> allAssociatedSourceRules = (Set<SourceRuleModel>)modelContext.getOriginalValue("sourceRules");
        return (Set<SourceRuleModel>)allAssociatedSourceRules.stream().filter(r -> !r.getStatus().equals(RuleStatus.UNPUBLISHED)).collect(
                        Collectors.toSet());
    }


    protected L10NService getL10NService()
    {
        return this.l10NService;
    }


    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }
}
