package de.hybris.platform.ruleengineservices.rule.services.impl;

import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionDefinitionService;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsRegistry;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleConditionsRegistry implements RuleConditionsRegistry
{
    private RuleConditionDefinitionService ruleConditionDefinitionService;
    private Converter<RuleConditionDefinitionModel, RuleConditionDefinitionData> ruleConditionDefinitionConverter;


    public List<RuleConditionDefinitionData> getAllConditionDefinitions()
    {
        return convertConditionDefinitions(this.ruleConditionDefinitionService
                        .getAllRuleConditionDefinitions());
    }


    public Map<String, RuleConditionDefinitionData> getAllConditionDefinitionsAsMap()
    {
        List<RuleConditionDefinitionData> conditionDefinitions = convertConditionDefinitions(this.ruleConditionDefinitionService
                        .getAllRuleConditionDefinitions());
        Map<String, RuleConditionDefinitionData> result = new HashMap<>();
        for(RuleConditionDefinitionData conditionDefinition : conditionDefinitions)
        {
            result.put(conditionDefinition.getId(), conditionDefinition);
        }
        return result;
    }


    public List<RuleConditionDefinitionData> getConditionDefinitionsForRuleType(Class<?> ruleType)
    {
        return convertConditionDefinitions(this.ruleConditionDefinitionService
                        .getRuleConditionDefinitionsForRuleType(ruleType));
    }


    public Map<String, RuleConditionDefinitionData> getConditionDefinitionsForRuleTypeAsMap(Class<?> ruleType)
    {
        List<RuleConditionDefinitionData> conditionDefinitions = convertConditionDefinitions(this.ruleConditionDefinitionService
                        .getRuleConditionDefinitionsForRuleType(ruleType));
        Map<String, RuleConditionDefinitionData> result = new HashMap<>();
        for(RuleConditionDefinitionData actionDefinition : conditionDefinitions)
        {
            result.put(actionDefinition.getId(), actionDefinition);
        }
        return result;
    }


    protected List<RuleConditionDefinitionData> convertConditionDefinitions(List<RuleConditionDefinitionModel> definitions)
    {
        List<RuleConditionDefinitionData> definitionsData = new ArrayList<>();
        definitions.stream().forEach(model -> definitionsData.add((RuleConditionDefinitionData)this.ruleConditionDefinitionConverter.convert(model)));
        return definitionsData;
    }


    public RuleConditionDefinitionService getRuleConditionDefinitionService()
    {
        return this.ruleConditionDefinitionService;
    }


    @Required
    public void setRuleConditionDefinitionService(RuleConditionDefinitionService ruleConditionDefinitionService)
    {
        this.ruleConditionDefinitionService = ruleConditionDefinitionService;
    }


    public Converter<RuleConditionDefinitionModel, RuleConditionDefinitionData> getRuleConditionDefinitionConverter()
    {
        return this.ruleConditionDefinitionConverter;
    }


    @Required
    public void setRuleConditionDefinitionConverter(Converter<RuleConditionDefinitionModel, RuleConditionDefinitionData> ruleConditionDefinitionConverter)
    {
        this.ruleConditionDefinitionConverter = ruleConditionDefinitionConverter;
    }
}
