package de.hybris.platform.ruleengineservices.rule.strategies;

import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import java.util.List;
import java.util.Map;

public interface RuleConditionsConverter
{
    String toString(List<RuleConditionData> paramList, Map<String, RuleConditionDefinitionData> paramMap);


    List<RuleConditionData> fromString(String paramString, Map<String, RuleConditionDefinitionData> paramMap);
}
