package de.hybris.platform.ruleengineservices.compiler;

public interface RuleIrVariablesGenerator
{
    public static final String DEFAULT_VARIABLES_CONTAINER_ID = "default";
    public static final String CONTAINER_PATH_SEPARATOR = "/";


    RuleIrVariablesContainer getRootContainer();


    RuleIrVariablesContainer getCurrentContainer();


    RuleIrVariablesContainer createContainer(String paramString);


    void closeContainer();


    String generateVariable(Class<?> paramClass);


    RuleIrLocalVariablesContainer createLocalContainer();


    String generateLocalVariable(RuleIrLocalVariablesContainer paramRuleIrLocalVariablesContainer, Class<?> paramClass);
}
