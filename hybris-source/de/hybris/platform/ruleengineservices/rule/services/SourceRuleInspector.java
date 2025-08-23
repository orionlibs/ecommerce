package de.hybris.platform.ruleengineservices.rule.services;

import de.hybris.platform.ruleengineservices.model.SourceRuleModel;

public interface SourceRuleInspector
{
    boolean hasRuleCondition(SourceRuleModel paramSourceRuleModel, String paramString);


    boolean hasRuleAction(SourceRuleModel paramSourceRuleModel, String paramString);
}
