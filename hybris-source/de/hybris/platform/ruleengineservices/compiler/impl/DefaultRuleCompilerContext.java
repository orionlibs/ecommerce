package de.hybris.platform.ruleengineservices.compiler.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleIrLocalVariablesContainer;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesGenerator;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.List;
import java.util.Map;

public class DefaultRuleCompilerContext implements RuleCompilerContext
{
    private final AbstractRuleModel rule;
    private long ruleVersion;
    private final String moduleName;
    private final List<RuleParameterData> ruleParameters;
    private final List<RuleConditionData> ruleConditions;
    private final RuleIrVariablesGenerator variablesGenerator;
    private final Map<String, Object> attributes;
    private final List<Exception> failureExceptions;
    private final Map<String, RuleConditionDefinitionData> conditionDefinitions;
    private final Map<String, RuleActionDefinitionData> actionDefinitions;
    private RuleCompilationContext ruleCompilationContext;
    private final List<RuleCompilerProblem> problems;


    public DefaultRuleCompilerContext(RuleCompilationContext ruleCompilationContext, AbstractRuleModel rule, String moduleName, RuleIrVariablesGenerator variablesGenerator)
    {
        this.ruleCompilationContext = ruleCompilationContext;
        this.rule = rule;
        this.moduleName = moduleName;
        this.ruleParameters = Lists.newArrayList();
        this.variablesGenerator = variablesGenerator;
        this.attributes = Maps.newHashMap();
        this.failureExceptions = Lists.newArrayList();
        this.conditionDefinitions = Maps.newHashMap();
        this.actionDefinitions = Maps.newHashMap();
        this.problems = Lists.newArrayList();
        this.ruleConditions = Lists.newArrayList();
    }


    public AbstractRuleModel getRule()
    {
        return this.rule;
    }


    public String getModuleName()
    {
        return this.moduleName;
    }


    public List<RuleParameterData> getRuleParameters()
    {
        return this.ruleParameters;
    }


    public RuleIrVariablesGenerator getVariablesGenerator()
    {
        return this.variablesGenerator;
    }


    public String generateVariable(Class<?> type)
    {
        return this.variablesGenerator.generateVariable(type);
    }


    public RuleIrLocalVariablesContainer createLocalContainer()
    {
        return this.variablesGenerator.createLocalContainer();
    }


    public String generateLocalVariable(RuleIrLocalVariablesContainer container, Class<?> type)
    {
        return this.variablesGenerator.generateLocalVariable(container, type);
    }


    public Map<String, Object> getAttributes()
    {
        return this.attributes;
    }


    public void addFailureException(Exception exception)
    {
        this.failureExceptions.add(exception);
    }


    public List<Exception> getFailureExceptions()
    {
        return this.failureExceptions;
    }


    public List<RuleCompilerProblem> getProblems()
    {
        return this.problems;
    }


    public void addProblem(RuleCompilerProblem problem)
    {
        this.problems.add(problem);
    }


    public Map<String, RuleConditionDefinitionData> getConditionDefinitions()
    {
        return this.conditionDefinitions;
    }


    public Map<String, RuleActionDefinitionData> getActionDefinitions()
    {
        return this.actionDefinitions;
    }


    public List<RuleConditionData> getRuleConditions()
    {
        return this.ruleConditions;
    }


    public RuleCompilationContext getRuleCompilationContext()
    {
        return this.ruleCompilationContext;
    }


    public long getRuleVersion()
    {
        return this.ruleVersion;
    }


    public void setRuleVersion(long version)
    {
        this.ruleVersion = version;
    }
}
