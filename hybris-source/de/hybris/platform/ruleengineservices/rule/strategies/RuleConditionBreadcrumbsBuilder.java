package de.hybris.platform.ruleengineservices.rule.strategies;

import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import java.util.List;
import java.util.Map;

public interface RuleConditionBreadcrumbsBuilder
{
    String buildConditionBreadcrumbs(List<RuleConditionData> paramList, Map<String, RuleConditionDefinitionData> paramMap);


    String buildStyledConditionBreadcrumbs(List<RuleConditionData> paramList, Map<String, RuleConditionDefinitionData> paramMap);
}
