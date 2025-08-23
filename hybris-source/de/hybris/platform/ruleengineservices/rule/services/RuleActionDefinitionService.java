package de.hybris.platform.ruleengineservices.rule.services;

import de.hybris.platform.ruleengineservices.model.RuleActionDefinitionModel;
import java.util.List;

public interface RuleActionDefinitionService
{
    List<RuleActionDefinitionModel> getAllRuleActionDefinitions();


    List<RuleActionDefinitionModel> getRuleActionDefinitionsForRuleType(Class<?> paramClass);
}
