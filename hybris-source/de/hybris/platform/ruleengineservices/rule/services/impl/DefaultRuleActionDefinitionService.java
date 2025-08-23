package de.hybris.platform.ruleengineservices.rule.services.impl;

import de.hybris.platform.ruleengineservices.model.RuleActionDefinitionModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleActionDefinitionDao;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionDefinitionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleActionDefinitionService implements RuleActionDefinitionService
{
    private RuleActionDefinitionDao ruleActionDefinitionDao;


    public List<RuleActionDefinitionModel> getAllRuleActionDefinitions()
    {
        return this.ruleActionDefinitionDao.findAllRuleActionDefinitions();
    }


    public List<RuleActionDefinitionModel> getRuleActionDefinitionsForRuleType(Class<?> ruleType)
    {
        return this.ruleActionDefinitionDao.findRuleActionDefinitionsByRuleType(ruleType);
    }


    public RuleActionDefinitionDao getRuleActionDefinitionDao()
    {
        return this.ruleActionDefinitionDao;
    }


    @Required
    public void setRuleActionDefinitionDao(RuleActionDefinitionDao ruleActionDefinitionDao)
    {
        this.ruleActionDefinitionDao = ruleActionDefinitionDao;
    }
}
