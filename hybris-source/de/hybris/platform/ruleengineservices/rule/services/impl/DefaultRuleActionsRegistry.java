package de.hybris.platform.ruleengineservices.rule.services.impl;

import de.hybris.platform.ruleengineservices.model.RuleActionDefinitionModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionDefinitionService;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsRegistry;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleActionsRegistry implements RuleActionsRegistry
{
    private RuleActionDefinitionService ruleActionDefinitionService;
    private Converter<RuleActionDefinitionModel, RuleActionDefinitionData> ruleActionDefinitionConverter;


    public List<RuleActionDefinitionData> getAllActionDefinitions()
    {
        return convertActionDefinitions(this.ruleActionDefinitionService.getAllRuleActionDefinitions());
    }


    public Map<String, RuleActionDefinitionData> getAllActionDefinitionsAsMap()
    {
        List<RuleActionDefinitionData> actionDefinitions = convertActionDefinitions(this.ruleActionDefinitionService
                        .getAllRuleActionDefinitions());
        Map<String, RuleActionDefinitionData> result = new HashMap<>();
        for(RuleActionDefinitionData actionDefinition : actionDefinitions)
        {
            result.put(actionDefinition.getId(), actionDefinition);
        }
        return result;
    }


    public List<RuleActionDefinitionData> getActionDefinitionsForRuleType(Class<?> ruleType)
    {
        return convertActionDefinitions(this.ruleActionDefinitionService
                        .getRuleActionDefinitionsForRuleType(ruleType));
    }


    public Map<String, RuleActionDefinitionData> getActionDefinitionsForRuleTypeAsMap(Class<?> ruleType)
    {
        List<RuleActionDefinitionData> actionDefinitions = convertActionDefinitions(this.ruleActionDefinitionService
                        .getRuleActionDefinitionsForRuleType(ruleType));
        Map<String, RuleActionDefinitionData> result = new HashMap<>();
        for(RuleActionDefinitionData actionDefinition : actionDefinitions)
        {
            result.put(actionDefinition.getId(), actionDefinition);
        }
        return result;
    }


    protected List<RuleActionDefinitionData> convertActionDefinitions(List<RuleActionDefinitionModel> definitions)
    {
        List<RuleActionDefinitionData> definitionsData = new ArrayList<>();
        definitions.stream().forEach(model -> definitionsData.add((RuleActionDefinitionData)this.ruleActionDefinitionConverter.convert(model)));
        return definitionsData;
    }


    public RuleActionDefinitionService getRuleActionDefinitionService()
    {
        return this.ruleActionDefinitionService;
    }


    @Required
    public void setRuleActionDefinitionService(RuleActionDefinitionService ruleActionDefinitionService)
    {
        this.ruleActionDefinitionService = ruleActionDefinitionService;
    }


    public Converter<RuleActionDefinitionModel, RuleActionDefinitionData> getRuleActionDefinitionConverter()
    {
        return this.ruleActionDefinitionConverter;
    }


    @Required
    public void setRuleActionDefinitionConverter(Converter<RuleActionDefinitionModel, RuleActionDefinitionData> ruleActionDefinitionConverter)
    {
        this.ruleActionDefinitionConverter = ruleActionDefinitionConverter;
    }
}
