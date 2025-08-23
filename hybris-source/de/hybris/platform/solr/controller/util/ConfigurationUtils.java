package de.hybris.platform.solr.controller.util;

import java.util.Map;

public final class ConfigurationUtils
{
    public static final boolean isFailOnError(Map<String, String> configuration)
    {
        String failOnError = configuration.get("solrserver.failOnError");
        if(StringUtils.isNotBlank(failOnError))
        {
            return Boolean.parseBoolean(failOnError);
        }
        return true;
    }


    public static final boolean isForceRestart(Map<String, String> configuration)
    {
        String forceRestart = configuration.get("solrserver.forceRestart");
        if(StringUtils.isNotBlank(forceRestart))
        {
            return Boolean.parseBoolean(forceRestart);
        }
        return true;
    }
}
