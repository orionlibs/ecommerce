package de.hybris.platform.ruleengine.cache;

@FunctionalInterface
public interface RuleGlobalsBeanProvider
{
    Object getRuleGlobals(String paramString);
}
