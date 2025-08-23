package de.hybris.bootstrap.loader.rule;

public interface IgnoreClassLoadingRule
{
    void initialize(String paramString);


    IgnoredStatus isIgnored(String paramString);
}
