package de.hybris.platform.ruleengineservices.compiler;

public interface RuleCompilerListener
{
    void beforeCompile(RuleCompilerContext paramRuleCompilerContext);


    void afterCompile(RuleCompilerContext paramRuleCompilerContext);


    void afterCompileError(RuleCompilerContext paramRuleCompilerContext);
}
