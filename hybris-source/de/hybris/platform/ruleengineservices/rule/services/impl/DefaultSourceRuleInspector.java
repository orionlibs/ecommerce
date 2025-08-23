package de.hybris.platform.ruleengineservices.rule.services.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsRegistry;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsRegistry;
import de.hybris.platform.ruleengineservices.rule.services.SourceRuleInspector;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleActionsConverter;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConditionsConverter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSourceRuleInspector implements SourceRuleInspector
{
    private RuleConditionsConverter ruleConditionsConverter;
    private RuleActionsConverter ruleActionsConverter;
    private RuleConditionsRegistry ruleConditionsRegistry;
    private RuleActionsRegistry ruleActionsRegistry;


    public boolean hasRuleCondition(SourceRuleModel sourceRule, String conditionDefinitionId)
    {
        Preconditions.checkArgument(Objects.nonNull(sourceRule), "sourceRule cannot be null");
        Preconditions.checkArgument(Objects.nonNull(conditionDefinitionId), "conditionDefinitionId cannot be null");
        Map<String, RuleConditionDefinitionData> ruleConditionDefinitions = getRuleConditionsRegistry().getConditionDefinitionsForRuleTypeAsMap(sourceRule.getClass());
        List<RuleConditionData> ruleConditionDatas = getRuleConditionsConverter().fromString(sourceRule.getConditions(), ruleConditionDefinitions);
        return collectAll(ruleConditionDatas).anyMatch(c -> conditionDefinitionId.equals(c.getDefinitionId()));
    }


    protected Stream<RuleConditionData> collectAll(List<RuleConditionData> ruleConditionDatas)
    {
        if(CollectionUtils.isEmpty(ruleConditionDatas))
        {
            return Stream.empty();
        }
        RuleConditionData head = head(ruleConditionDatas);
        if(ruleConditionDatas.size() == 1)
        {
            return Stream.concat(Stream.of(head), collectAll(head.getChildren()));
        }
        List<RuleConditionData> tail = tail(ruleConditionDatas);
        return Stream.concat(collectAll(tail), collectAll(Lists.newArrayList((Object[])new RuleConditionData[] {head})));
    }


    protected RuleConditionData head(List<RuleConditionData> list)
    {
        return list.get(0);
    }


    protected List<RuleConditionData> tail(List<RuleConditionData> list)
    {
        return list.subList(1, list.size());
    }


    public boolean hasRuleAction(SourceRuleModel sourceRule, String actionDefinitionId)
    {
        Preconditions.checkArgument(Objects.nonNull(sourceRule), "sourceRule cannot be null");
        Preconditions.checkArgument(Objects.nonNull(actionDefinitionId), "actionDefinitionId cannot be null");
        Map<String, RuleActionDefinitionData> actionDefinitionsForRuleType = getRuleActionsRegistry().getActionDefinitionsForRuleTypeAsMap(sourceRule.getClass());
        List<RuleActionData> ruleActionDatas = getRuleActionsConverter().fromString(sourceRule.getActions(), actionDefinitionsForRuleType);
        return ruleActionDatas.stream().anyMatch(c -> actionDefinitionId.equals(c.getDefinitionId()));
    }


    protected RuleConditionsConverter getRuleConditionsConverter()
    {
        return this.ruleConditionsConverter;
    }


    @Required
    public void setRuleConditionsConverter(RuleConditionsConverter ruleConditionsConverter)
    {
        this.ruleConditionsConverter = ruleConditionsConverter;
    }


    protected RuleActionsConverter getRuleActionsConverter()
    {
        return this.ruleActionsConverter;
    }


    @Required
    public void setRuleActionsConverter(RuleActionsConverter ruleActionsConverter)
    {
        this.ruleActionsConverter = ruleActionsConverter;
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


    protected RuleActionsRegistry getRuleActionsRegistry()
    {
        return this.ruleActionsRegistry;
    }


    @Required
    public void setRuleActionsRegistry(RuleActionsRegistry ruleActionsRegistry)
    {
        this.ruleActionsRegistry = ruleActionsRegistry;
    }
}
