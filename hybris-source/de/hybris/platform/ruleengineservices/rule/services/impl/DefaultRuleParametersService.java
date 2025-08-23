package de.hybris.platform.ruleengineservices.rule.services.impl;

import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleParametersService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterUuidGenerator;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParametersConverter;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleParametersService implements RuleParametersService
{
    private RuleParametersConverter ruleParametersConverter;
    private RuleParameterUuidGenerator ruleParameterUuidGenerator;


    public RuleParameterData createParameterFromDefinition(RuleParameterDefinitionData definition)
    {
        RuleParameterData parameter = new RuleParameterData();
        parameter.setUuid(this.ruleParameterUuidGenerator.generateUuid(parameter, definition));
        parameter.setType(definition.getType());
        parameter.setValue(definition.getDefaultValue());
        return parameter;
    }


    public String convertParametersToString(List<RuleParameterData> parameters)
    {
        return this.ruleParametersConverter.toString(parameters);
    }


    public List<RuleParameterData> convertParametersFromString(String parameters)
    {
        return this.ruleParametersConverter.fromString(parameters);
    }


    public RuleParametersConverter getRuleParametersConverter()
    {
        return this.ruleParametersConverter;
    }


    @Required
    public void setRuleParametersConverter(RuleParametersConverter ruleParametersConverter)
    {
        this.ruleParametersConverter = ruleParametersConverter;
    }


    public RuleParameterUuidGenerator getRuleParameterUuidGenerator()
    {
        return this.ruleParameterUuidGenerator;
    }


    @Required
    public void setRuleParameterUuidGenerator(RuleParameterUuidGenerator ruleParameterUuidGenerator)
    {
        this.ruleParameterUuidGenerator = ruleParameterUuidGenerator;
    }
}
