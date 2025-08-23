package de.hybris.platform.ruleengineservices.rule.services.impl;

import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsService;
import de.hybris.platform.ruleengineservices.rule.services.RuleParametersService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConditionBreadcrumbsBuilder;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConditionsConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleConditionsService implements RuleConditionsService
{
    private RuleConditionsConverter ruleConditionsConverter;
    private RuleConditionBreadcrumbsBuilder ruleConditionBreadcrumbsBuilder;
    private RuleParametersService ruleParametersService;


    public RuleConditionData createConditionFromDefinition(RuleConditionDefinitionData definition)
    {
        Map<String, RuleParameterData> parameters = new HashMap<>();
        for(Map.Entry<String, RuleParameterDefinitionData> entry : (Iterable<Map.Entry<String, RuleParameterDefinitionData>>)definition.getParameters().entrySet())
        {
            String parameterId = entry.getKey();
            RuleParameterDefinitionData parameterDefinition = entry.getValue();
            RuleParameterData parameter = this.ruleParametersService.createParameterFromDefinition(parameterDefinition);
            parameters.put(parameterId, parameter);
        }
        RuleConditionData condition = new RuleConditionData();
        condition.setDefinitionId(definition.getId());
        condition.setParameters(parameters);
        condition.setChildren(new ArrayList());
        return condition;
    }


    public String buildConditionBreadcrumbs(List<RuleConditionData> conditions, Map<String, RuleConditionDefinitionData> conditionDefinitions)
    {
        return this.ruleConditionBreadcrumbsBuilder.buildConditionBreadcrumbs(conditions, conditionDefinitions);
    }


    public String buildStyledConditionBreadcrumbs(List<RuleConditionData> conditions, Map<String, RuleConditionDefinitionData> conditionDefinitions)
    {
        return this.ruleConditionBreadcrumbsBuilder.buildStyledConditionBreadcrumbs(conditions, conditionDefinitions);
    }


    public String convertConditionsToString(List<RuleConditionData> conditions, Map<String, RuleConditionDefinitionData> conditionDefinitions)
    {
        return this.ruleConditionsConverter.toString(conditions, conditionDefinitions);
    }


    public List<RuleConditionData> convertConditionsFromString(String conditions, Map<String, RuleConditionDefinitionData> conditionDefinitions)
    {
        return this.ruleConditionsConverter.fromString(conditions, conditionDefinitions);
    }


    public RuleConditionsConverter getRuleConditionsConverter()
    {
        return this.ruleConditionsConverter;
    }


    @Required
    public void setRuleConditionsConverter(RuleConditionsConverter ruleConditionsConverter)
    {
        this.ruleConditionsConverter = ruleConditionsConverter;
    }


    public RuleConditionBreadcrumbsBuilder getRuleConditionBreadcrumbsBuilder()
    {
        return this.ruleConditionBreadcrumbsBuilder;
    }


    @Required
    public void setRuleConditionBreadcrumbsBuilder(RuleConditionBreadcrumbsBuilder ruleConditionBreadcrumbsBuilder)
    {
        this.ruleConditionBreadcrumbsBuilder = ruleConditionBreadcrumbsBuilder;
    }


    public RuleParametersService getRuleParametersService()
    {
        return this.ruleParametersService;
    }


    @Required
    public void setRuleParametersService(RuleParametersService ruleParametersService)
    {
        this.ruleParametersService = ruleParametersService;
    }
}
