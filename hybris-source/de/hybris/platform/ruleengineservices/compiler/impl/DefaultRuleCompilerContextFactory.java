package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContextFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesGenerator;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsRegistry;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsRegistry;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleCompilerContextFactory implements RuleCompilerContextFactory<DefaultRuleCompilerContext>
{
    private RuleConditionsRegistry ruleConditionsRegistry;
    private RuleActionsRegistry ruleActionsRegistry;


    public DefaultRuleCompilerContext createContext(RuleCompilationContext ruleCompilationContext, AbstractRuleModel rule, String moduleName, RuleIrVariablesGenerator variablesGenerator)
    {
        DefaultRuleCompilerContext context = new DefaultRuleCompilerContext(ruleCompilationContext, rule, moduleName, variablesGenerator);
        populateDefinitionsForRule(rule, context);
        return context;
    }


    protected void populateDefinitionsForRule(AbstractRuleModel rule, DefaultRuleCompilerContext context)
    {
        Map<String, RuleConditionDefinitionData> conditionDefinitions = this.ruleConditionsRegistry.getConditionDefinitionsForRuleTypeAsMap(rule.getClass());
        context.getConditionDefinitions().putAll(conditionDefinitions);
        Map<String, RuleActionDefinitionData> actionDefinitions = this.ruleActionsRegistry.getActionDefinitionsForRuleTypeAsMap(rule.getClass());
        context.getActionDefinitions().putAll(actionDefinitions);
    }


    public RuleConditionsRegistry getRuleConditionsRegistry()
    {
        return this.ruleConditionsRegistry;
    }


    @Required
    public void setRuleConditionsRegistry(RuleConditionsRegistry ruleConditionsRegistry)
    {
        this.ruleConditionsRegistry = ruleConditionsRegistry;
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
