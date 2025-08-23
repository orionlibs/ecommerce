package de.hybris.platform.droolsruleengineservices.compiler;

import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIr;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariable;
import java.util.Deque;
import java.util.Map;
import java.util.Set;

public interface DroolsRuleGeneratorContext
{
    RuleCompilerContext getRuleCompilerContext();


    String getIndentationSize();


    String getVariablePrefix();


    String getAttributeDelimiter();


    RuleIr getRuleIr();


    Map<String, RuleIrVariable> getVariables();


    Deque<Map<String, RuleIrVariable>> getLocalVariables();


    void addLocalVariables(Map<String, RuleIrVariable> paramMap);


    Set<Class<?>> getImports();


    Map<String, Class<?>> getGlobals();


    String generateClassName(Class<?> paramClass);


    void addGlobal(String paramString, Class<?> paramClass);


    DroolsRuleModel getDroolsRule();
}
