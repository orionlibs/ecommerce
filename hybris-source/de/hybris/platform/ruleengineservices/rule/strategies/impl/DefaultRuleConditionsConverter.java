package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.CollectionType;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleData;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsRegistry;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConditionsConverter;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConverterException;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterUuidGenerator;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleConditionsConverter extends AbstractRuleConverter implements RuleConditionsConverter
{
    private RuleConditionsRegistry ruleConditionsRegistry;
    private RuleParameterUuidGenerator ruleParameterUuidGenerator;


    public String toString(List<RuleConditionData> conditions, Map<String, RuleConditionDefinitionData> conditionDefinitions)
    {
        try
        {
            return getObjectWriter().writeValueAsString(conditions);
        }
        catch(IOException e)
        {
            throw new RuleConverterException(e);
        }
    }


    public List<RuleConditionData> fromString(String conditions, Map<String, RuleConditionDefinitionData> conditionDefinitions)
    {
        if(StringUtils.isBlank(conditions))
        {
            return Collections.emptyList();
        }
        if(MapUtils.isEmpty(conditionDefinitions))
        {
            return Collections.emptyList();
        }
        try
        {
            ObjectReader objectReader = getObjectReader();
            CollectionType collectionType = objectReader.getTypeFactory().constructCollectionType(List.class, RuleConditionData.class);
            List<RuleConditionData> parsedConditions = (List<RuleConditionData>)objectReader.forType((JavaType)collectionType).readValue(conditions);
            convertParameterValues(conditionDefinitions, parsedConditions);
            return parsedConditions;
        }
        catch(IOException e)
        {
            throw new RuleConverterException(e);
        }
    }


    protected void convertParameterValues(Map<String, RuleConditionDefinitionData> conditionDefinitions, List<RuleConditionData> conditions)
    {
        if(CollectionUtils.isEmpty(conditions))
        {
            return;
        }
        for(RuleConditionData condition : conditions)
        {
            RuleConditionDefinitionData conditionDefinition = conditionDefinitions.get(condition.getDefinitionId());
            if(conditionDefinition == null)
            {
                throw new RuleConverterException("No definition found for condition: [definitionId=" + condition.getDefinitionId() + "]");
            }
            if(condition.getParameters() == null)
            {
                condition.setParameters(new HashMap<>());
            }
            if(MapUtils.isEmpty(conditionDefinition.getParameters()))
            {
                condition.getParameters().clear();
            }
            else
            {
                condition.getParameters().keySet().retainAll(conditionDefinition.getParameters().keySet());
                convertParameters((AbstractRuleData)condition, (AbstractRuleDefinitionData)conditionDefinition);
            }
            convertParameterValues(conditionDefinitions, condition.getChildren());
        }
    }


    protected RuleConditionsRegistry getRuleConditionsRegistry()
    {
        return this.ruleConditionsRegistry;
    }


    @Required
    public void setRuleConditionsRegistry(RuleConditionsRegistry ruleConditionsRegistry)
    {
        this.ruleConditionsRegistry = ruleConditionsRegistry;
    }


    protected RuleParameterUuidGenerator getRuleParameterUuidGenerator()
    {
        return this.ruleParameterUuidGenerator;
    }


    @Required
    public void setRuleParameterUuidGenerator(RuleParameterUuidGenerator ruleParameterUuidGenerator)
    {
        this.ruleParameterUuidGenerator = ruleParameterUuidGenerator;
    }
}
