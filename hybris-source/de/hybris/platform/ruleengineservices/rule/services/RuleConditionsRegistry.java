package de.hybris.platform.ruleengineservices.rule.services;

import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import java.util.List;
import java.util.Map;

public interface RuleConditionsRegistry
{
    List<RuleConditionDefinitionData> getAllConditionDefinitions();


    Map<String, RuleConditionDefinitionData> getAllConditionDefinitionsAsMap();


    List<RuleConditionDefinitionData> getConditionDefinitionsForRuleType(Class<?> paramClass);


    Map<String, RuleConditionDefinitionData> getConditionDefinitionsForRuleTypeAsMap(Class<?> paramClass);
}
