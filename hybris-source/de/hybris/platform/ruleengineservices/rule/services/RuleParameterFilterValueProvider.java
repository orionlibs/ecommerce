package de.hybris.platform.ruleengineservices.rule.services;

public interface RuleParameterFilterValueProvider
{
    String getParameterId(String paramString);


    Object evaluate(String paramString, Object paramObject);
}
