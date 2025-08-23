package de.hybris.platform.ruleengineservices.rule.services;

import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionModel;
import java.util.List;

public interface RuleConditionDefinitionService
{
    List<RuleConditionDefinitionModel> getAllRuleConditionDefinitions();


    List<RuleConditionDefinitionModel> getRuleConditionDefinitionsForRuleType(Class<?> paramClass);
}
