package de.hybris.platform.ruleengineservices.rule.services;

import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import java.util.List;
import java.util.Map;

public interface RuleActionsRegistry
{
    List<RuleActionDefinitionData> getAllActionDefinitions();


    Map<String, RuleActionDefinitionData> getAllActionDefinitionsAsMap();


    List<RuleActionDefinitionData> getActionDefinitionsForRuleType(Class<?> paramClass);


    Map<String, RuleActionDefinitionData> getActionDefinitionsForRuleTypeAsMap(Class<?> paramClass);
}
