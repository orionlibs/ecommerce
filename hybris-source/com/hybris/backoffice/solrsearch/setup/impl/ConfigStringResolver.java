package com.hybris.backoffice.solrsearch.setup.impl;

import de.hybris.platform.util.Config;

@Deprecated(since = "2105")
public class ConfigStringResolver
{
    protected String resolveConfigStringParameter(String key)
    {
        return Config.getParameter(key);
    }
}
