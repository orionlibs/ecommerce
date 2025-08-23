package de.hybris.platform.ruleengineservices.rule.services.impl;

import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsService;
import de.hybris.platform.ruleengineservices.rule.services.RuleParametersService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleActionBreadcrumbsBuilder;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleActionsConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleActionsService implements RuleActionsService
{
    private RuleActionsConverter ruleActionsConverter;
    private RuleActionBreadcrumbsBuilder ruleActionBreadcrumbsBuilder;
    private RuleParametersService ruleParametersService;


    public RuleActionData createActionFromDefinition(RuleActionDefinitionData definition)
    {
        Map<String, RuleParameterData> parameters = new HashMap<>();
        for(Map.Entry<String, RuleParameterDefinitionData> entry : (Iterable<Map.Entry<String, RuleParameterDefinitionData>>)definition.getParameters().entrySet())
        {
            String parameterId = entry.getKey();
            RuleParameterDefinitionData parameterDefinition = entry.getValue();
            RuleParameterData parameter = this.ruleParametersService.createParameterFromDefinition(parameterDefinition);
            parameters.put(parameterId, parameter);
        }
        RuleActionData action = new RuleActionData();
        action.setDefinitionId(definition.getId());
        action.setParameters(parameters);
        return action;
    }


    public String buildActionBreadcrumbs(List<RuleActionData> actions, Map<String, RuleActionDefinitionData> actionDefinitions)
    {
        return this.ruleActionBreadcrumbsBuilder.buildActionBreadcrumbs(actions, actionDefinitions);
    }


    public String buildStyledActionBreadcrumbs(List<RuleActionData> actions, Map<String, RuleActionDefinitionData> actionDefinitions)
    {
        return this.ruleActionBreadcrumbsBuilder.buildStyledActionBreadcrumbs(actions, actionDefinitions);
    }


    public String convertActionsToString(List<RuleActionData> actions, Map<String, RuleActionDefinitionData> actionDefinitions)
    {
        return this.ruleActionsConverter.toString(actions, actionDefinitions);
    }


    public List<RuleActionData> convertActionsFromString(String actions, Map<String, RuleActionDefinitionData> actionDefinitions)
    {
        return this.ruleActionsConverter.fromString(actions, actionDefinitions);
    }


    public RuleActionsConverter getRuleActionsConverter()
    {
        return this.ruleActionsConverter;
    }


    @Required
    public void setRuleActionsConverter(RuleActionsConverter ruleActionsConverter)
    {
        this.ruleActionsConverter = ruleActionsConverter;
    }


    public RuleActionBreadcrumbsBuilder getRuleActionBreadcrumbsBuilder()
    {
        return this.ruleActionBreadcrumbsBuilder;
    }


    @Required
    public void setRuleActionBreadcrumbsBuilder(RuleActionBreadcrumbsBuilder ruleActionBreadcrumbsBuilder)
    {
        this.ruleActionBreadcrumbsBuilder = ruleActionBreadcrumbsBuilder;
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
