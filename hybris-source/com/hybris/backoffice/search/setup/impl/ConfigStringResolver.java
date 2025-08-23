package com.hybris.backoffice.search.setup.impl;

import de.hybris.platform.util.Config;

public class ConfigStringResolver
{
    public String resolveConfigStringParameter(String key)
    {
        return Config.getParameter(key);
    }
}
