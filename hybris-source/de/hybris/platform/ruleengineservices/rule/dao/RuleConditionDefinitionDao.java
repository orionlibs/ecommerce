package de.hybris.platform.ruleengineservices.rule.dao;

import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionModel;
import java.util.List;

public interface RuleConditionDefinitionDao
{
    List<RuleConditionDefinitionModel> findAllRuleConditionDefinitions();


    List<RuleConditionDefinitionModel> findRuleConditionDefinitionsByRuleType(Class<?> paramClass);
}
