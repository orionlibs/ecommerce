package de.hybris.platform.ruleengineservices.rule.strategies;

public interface RuleParameterValueConverter
{
    String toString(Object paramObject);


    Object fromString(String paramString1, String paramString2);
}
