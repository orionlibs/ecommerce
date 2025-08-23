package de.hybris.platform.ruleengineservices.rule.services;

import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import java.util.List;
import java.util.Map;

public interface RuleConditionsService
{
    RuleConditionData createConditionFromDefinition(RuleConditionDefinitionData paramRuleConditionDefinitionData);


    String buildConditionBreadcrumbs(List<RuleConditionData> paramList, Map<String, RuleConditionDefinitionData> paramMap);


    String buildStyledConditionBreadcrumbs(List<RuleConditionData> paramList, Map<String, RuleConditionDefinitionData> paramMap);


    String convertConditionsToString(List<RuleConditionData> paramList, Map<String, RuleConditionDefinitionData> paramMap);


    List<RuleConditionData> convertConditionsFromString(String paramString, Map<String, RuleConditionDefinitionData> paramMap);
}
