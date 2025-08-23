package de.hybris.platform.droolsruleengineservices.compiler;

public interface DroolsRuleConditionsGenerator
{
    String generateConditions(DroolsRuleGeneratorContext paramDroolsRuleGeneratorContext, String paramString);


    String generateRequiredFactsCheckPattern(DroolsRuleGeneratorContext paramDroolsRuleGeneratorContext);


    String generateRequiredTypeVariables(DroolsRuleGeneratorContext paramDroolsRuleGeneratorContext);
}
