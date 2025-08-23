package de.hybris.platform.ruleengineservices.versioning.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;
import org.assertj.core.util.Sets;
import org.mockito.Mockito;

class HistoricalRuleVersionTestHelper
{
    static final String RULE_CODE = "TEST_RULE_CODE";
    static final String ASSOCIATED_RULE_CODE = "ASSOCIATED_TEST_RULE_CODE";


    AbstractRuleModel createSourceRuleModel(String ruleCode, RuleStatus status)
    {
        AbstractRuleModel rule = (AbstractRuleModel)Mockito.mock(SourceRuleModel.class);
        Mockito.lenient().when(rule.getStatus()).thenReturn(status);
        Mockito.lenient().when(rule.getCode()).thenReturn(ruleCode);
        ItemModelContext ruleModelContext = (ItemModelContext)Mockito.mock(ItemModelContext.class);
        Mockito.lenient().when(rule.getItemModelContext()).thenReturn(ruleModelContext);
        Set<CampaignModel> origCampaigns = Sets.newHashSet();
        Mockito.lenient().when(ruleModelContext.getOriginalValue("campaigns")).thenReturn(origCampaigns);
        return rule;
    }


    AbstractRuleModel createSourceRuleModelWithCampaign(String ruleCode, RuleStatus status, CampaignModel campaign)
    {
        AbstractRuleModel rule = (AbstractRuleModel)Mockito.mock(SourceRuleModel.class);
        Mockito.lenient().when(rule.getStatus()).thenReturn(status);
        Mockito.lenient().when(rule.getCode()).thenReturn(ruleCode);
        ItemModelContext ruleModelContext = (ItemModelContext)Mockito.mock(ItemModelContext.class);
        Mockito.lenient().when(rule.getItemModelContext()).thenReturn(ruleModelContext);
        ImmutableSet immutableSet = ImmutableSet.of(campaign);
        Mockito.lenient().when(ruleModelContext.getOriginalValue("campaigns")).thenReturn(immutableSet);
        return rule;
    }
}
