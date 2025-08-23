package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.CollectionType;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleData;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsRegistry;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleActionsConverter;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConverterException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleActionsConverter extends AbstractRuleConverter implements RuleActionsConverter
{
    private RuleActionsRegistry ruleActionsRegistry;


    public String toString(List<RuleActionData> actions, Map<String, RuleActionDefinitionData> actionDefinitions)
    {
        try
        {
            return getObjectWriter().writeValueAsString(actions);
        }
        catch(IOException e)
        {
            throw new RuleConverterException(e);
        }
    }


    public List<RuleActionData> fromString(String actions, Map<String, RuleActionDefinitionData> actionDefinitions)
    {
        if(StringUtils.isBlank(actions))
        {
            return Collections.emptyList();
        }
        if(MapUtils.isEmpty(actionDefinitions))
        {
            return Collections.emptyList();
        }
        try
        {
            ObjectReader objectReader = getObjectReader();
            CollectionType collectionType = objectReader.getTypeFactory().constructCollectionType(List.class, RuleActionData.class);
            List<RuleActionData> parsedActions = (List<RuleActionData>)objectReader.forType((JavaType)collectionType).readValue(actions);
            convertParameterValues(parsedActions, actionDefinitions);
            return parsedActions;
        }
        catch(IOException e)
        {
            throw new RuleConverterException(e);
        }
    }


    protected void convertParameterValues(List<RuleActionData> actions, Map<String, RuleActionDefinitionData> actionDefinitions)
    {
        if(CollectionUtils.isEmpty(actions))
        {
            return;
        }
        for(RuleActionData action : actions)
        {
            RuleActionDefinitionData actionDefinition = actionDefinitions.get(action.getDefinitionId());
            if(actionDefinition == null)
            {
                throw new RuleConverterException("No definition found for action: [definitionId=" + action.getDefinitionId() + "]");
            }
            if(action.getParameters() == null)
            {
                action.setParameters(new HashMap<>());
            }
            if(MapUtils.isEmpty(actionDefinition.getParameters()))
            {
                action.getParameters().clear();
                continue;
            }
            action.getParameters().keySet().retainAll(actionDefinition.getParameters().keySet());
            convertParameters((AbstractRuleData)action, (AbstractRuleDefinitionData)actionDefinition);
        }
    }


    public RuleActionsRegistry getRuleActionsRegistry()
    {
        return this.ruleActionsRegistry;
    }


    @Required
    public void setRuleActionsRegistry(RuleActionsRegistry ruleActionsRegistry)
    {
        this.ruleActionsRegistry = ruleActionsRegistry;
    }
}
