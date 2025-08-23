package de.hybris.platform.ruleengineservices.rule.dao;

import de.hybris.platform.ruleengineservices.model.RuleActionDefinitionModel;
import java.util.List;

public interface RuleActionDefinitionDao
{
    List<RuleActionDefinitionModel> findAllRuleActionDefinitions();


    List<RuleActionDefinitionModel> findRuleActionDefinitionsByRuleType(Class<?> paramClass);
}
