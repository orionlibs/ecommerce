package de.hybris.platform.property.interceptor.impl;

import de.hybris.platform.property.interceptor.ServerStartupPropertiesInterceptor;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class ServerStartupLog4jPropertiesInterceptor implements ServerStartupPropertiesInterceptor
{
    private static final String WARN_LOG4J_DEPRECATED_PROPERTIES = "log4j.deprecated.properties.warn";
    private static final String PRINT_LOG4J_DEPRECATED_PROPERTIES = "log4j.deprecated.properties.print";
    private static final String LOG4J = "log4j.logger.";
    private final List<String> registeredLog4jProperties = new ArrayList<>();
    private final ConfigIntf cfg;
    private final Logger log;


    public ServerStartupLog4jPropertiesInterceptor(ConfigIntf cfg, Logger log)
    {
        this.cfg = cfg;
        this.log = log;
    }


    public void actionDuringIteration(Map.Entry<String, String> entry)
    {
        if(isLog4jRelated(entry))
        {
            this.registeredLog4jProperties.add(entry.getKey());
        }
    }


    public void actionAfterIteration()
    {
        if(!this.registeredLog4jProperties.isEmpty() && isLog4jWarningEnabled(this.cfg))
        {
            this.log.warn("[DETECTED USAGE OF LOG4J PROPERTIES] Support for log4j has been abandoned. Please use log4j2 instead." +
                            informAboutPrintLog4jProperty(this.cfg));
            if(shouldPrintUsedLog4jProperties(this.cfg))
            {
                this.registeredLog4jProperties.forEach(p -> this.log.warn("Unsupported log4j property: " + p));
            }
        }
    }


    private String informAboutPrintLog4jProperty(ConfigIntf cfg)
    {
        if(shouldPrintUsedLog4jProperties(cfg))
        {
            return "";
        }
        return " Set <log4j.deprecated.properties.print> to 'true' to see all properties that should be changed/removed";
    }


    private boolean shouldPrintUsedLog4jProperties(ConfigIntf cfg)
    {
        return cfg.getBoolean("log4j.deprecated.properties.print", false);
    }


    private boolean isLog4jWarningEnabled(ConfigIntf cfg)
    {
        return cfg.getBoolean("log4j.deprecated.properties.warn", true);
    }


    private boolean isLog4jRelated(Map.Entry<String, String> entry)
    {
        String trimmedKey = ((String)entry.getKey()).trim();
        return trimmedKey.startsWith("log4j.logger.");
    }
}
