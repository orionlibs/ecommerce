package de.hybris.platform.ruleengineservices.rule.services;

import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import java.util.List;
import java.util.Map;

public interface RuleActionsService
{
    RuleActionData createActionFromDefinition(RuleActionDefinitionData paramRuleActionDefinitionData);


    String buildActionBreadcrumbs(List<RuleActionData> paramList, Map<String, RuleActionDefinitionData> paramMap);


    String buildStyledActionBreadcrumbs(List<RuleActionData> paramList, Map<String, RuleActionDefinitionData> paramMap);


    String convertActionsToString(List<RuleActionData> paramList, Map<String, RuleActionDefinitionData> paramMap);


    List<RuleActionData> convertActionsFromString(String paramString, Map<String, RuleActionDefinitionData> paramMap);
}
