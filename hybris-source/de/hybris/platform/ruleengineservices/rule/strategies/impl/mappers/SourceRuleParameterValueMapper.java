package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class SourceRuleParameterValueMapper implements RuleParameterValueMapper<AbstractRuleModel>
{
    private RuleService ruleService;


    public AbstractRuleModel fromString(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "String value cannot be null");
        try
        {
            return getRuleService().getRuleForCode(code);
        }
        catch(ModelNotFoundException ex)
        {
            throw new RuleParameterValueMapperException("Cannot find rule with the code: " + code);
        }
    }


    public String toString(AbstractRuleModel sourceRule)
    {
        ServicesUtil.validateParameterNotNull(sourceRule, "Object cannot be null");
        return sourceRule.getCode();
    }


    protected RuleService getRuleService()
    {
        return this.ruleService;
    }


    @Required
    public void setRuleService(RuleService ruleService)
    {
        this.ruleService = ruleService;
    }
}
