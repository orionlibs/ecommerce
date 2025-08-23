package de.hybris.platform.ruleengineservices.rule.services.impl;

import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleConditionDefinitionDao;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionDefinitionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleConditionDefinitionService implements RuleConditionDefinitionService
{
    private RuleConditionDefinitionDao ruleConditionDefinitionDao;


    public List<RuleConditionDefinitionModel> getAllRuleConditionDefinitions()
    {
        return this.ruleConditionDefinitionDao.findAllRuleConditionDefinitions();
    }


    public List<RuleConditionDefinitionModel> getRuleConditionDefinitionsForRuleType(Class<?> ruleType)
    {
        return this.ruleConditionDefinitionDao.findRuleConditionDefinitionsByRuleType(ruleType);
    }


    public RuleConditionDefinitionDao getRuleConditionDefinitionDao()
    {
        return this.ruleConditionDefinitionDao;
    }


    @Required
    public void setRuleConditionDefinitionDao(RuleConditionDefinitionDao ruleConditionDefinitionDao)
    {
        this.ruleConditionDefinitionDao = ruleConditionDefinitionDao;
    }
}
