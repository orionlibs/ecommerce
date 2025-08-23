package de.hybris.platform.ruleengineservices.rule.strategies;

public interface RuleParameterValueMapper<T>
{
    String toString(T paramT);


    T fromString(String paramString);
}
