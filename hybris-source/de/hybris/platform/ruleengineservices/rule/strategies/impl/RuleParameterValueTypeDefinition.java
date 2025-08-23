package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueNormalizer;
import org.springframework.beans.factory.annotation.Required;

public class RuleParameterValueTypeDefinition
{
    private RuleParameterValueMapper mapper;
    private RuleParameterValueNormalizer normalizer;
    private String type;


    public RuleParameterValueMapper getMapper()
    {
        return this.mapper;
    }


    @Required
    public void setMapper(RuleParameterValueMapper mapper)
    {
        this.mapper = mapper;
    }


    public RuleParameterValueNormalizer getNormalizer()
    {
        return this.normalizer;
    }


    public void setNormalizer(RuleParameterValueNormalizer normalizer)
    {
        this.normalizer = normalizer;
    }


    public String getType()
    {
        return this.type;
    }


    @Required
    public void setType(String type)
    {
        this.type = type;
    }
}
