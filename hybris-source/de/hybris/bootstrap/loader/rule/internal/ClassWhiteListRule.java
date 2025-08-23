package de.hybris.bootstrap.loader.rule.internal;

import de.hybris.bootstrap.loader.rule.IgnoreClassLoadingRule;

public class ClassWhiteListRule extends AbstractClassListRule
{
    public IgnoreClassLoadingRule.IgnoredStatus isIgnored(String name)
    {
        return isIgnored(name, IgnoreClassLoadingRule.IgnoredStatus.WHITELISTED);
    }
}
