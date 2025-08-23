package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIr;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrProcessor;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rao.CampaignRAO;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public class DefaultSourceRuleIrProcessor implements RuleIrProcessor
{
    private static final String CAMPAIGN_RAO_CODE_ATTRIBUTE = "code";


    public void process(RuleCompilerContext context, RuleIr ruleIr)
    {
        AbstractRuleModel rule = context.getRule();
        if(rule instanceof SourceRuleModel)
        {
            SourceRuleModel sourceRule = (SourceRuleModel)rule;
            if(CollectionUtils.isNotEmpty(sourceRule.getCampaigns()))
            {
                String campaignRaoVariable = context.generateVariable(CampaignRAO.class);
                RuleIrAttributeCondition irAttributeCondition = new RuleIrAttributeCondition();
                irAttributeCondition.setAttribute("code");
                irAttributeCondition.setOperator(RuleIrAttributeOperator.IN);
                irAttributeCondition
                                .setValue(sourceRule.getCampaigns().stream().map(CampaignModel::getCode).collect(Collectors.toList()));
                irAttributeCondition.setVariable(campaignRaoVariable);
                ruleIr.getConditions().add(irAttributeCondition);
            }
        }
    }
}
