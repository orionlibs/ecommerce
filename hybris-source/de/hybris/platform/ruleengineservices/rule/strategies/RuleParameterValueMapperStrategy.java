package de.hybris.platform.ruleengineservices.rule.strategies;

public interface RuleParameterValueMapperStrategy
{
    Object toRuleParameter(Object paramObject, String paramString);


    Object fromRuleParameter(Object paramObject, String paramString);
}
