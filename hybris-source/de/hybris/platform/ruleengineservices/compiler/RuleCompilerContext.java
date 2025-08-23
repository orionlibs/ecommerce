package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.List;
import java.util.Map;

public interface RuleCompilerContext
{
    AbstractRuleModel getRule();


    long getRuleVersion();


    void setRuleVersion(long paramLong);


    String getModuleName();


    List<RuleParameterData> getRuleParameters();


    RuleIrVariablesGenerator getVariablesGenerator();


    String generateVariable(Class<?> paramClass);


    RuleIrLocalVariablesContainer createLocalContainer();


    String generateLocalVariable(RuleIrLocalVariablesContainer paramRuleIrLocalVariablesContainer, Class<?> paramClass);


    Map<String, Object> getAttributes();


    List<Exception> getFailureExceptions();


    List<RuleCompilerProblem> getProblems();


    void addProblem(RuleCompilerProblem paramRuleCompilerProblem);


    Map<String, RuleConditionDefinitionData> getConditionDefinitions();


    Map<String, RuleActionDefinitionData> getActionDefinitions();


    List<RuleConditionData> getRuleConditions();


    RuleCompilationContext getRuleCompilationContext();
}
