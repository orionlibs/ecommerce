package de.hybris.platform.property.interceptor.impl;

import de.hybris.platform.property.interceptor.ServerStartupPropertiesInterceptor;
import java.util.Map;
import org.apache.log4j.Logger;

public class ServerStartupTomcatPropertiesInterceptor implements ServerStartupPropertiesInterceptor
{
    private static final String TOMCAT_DISABLE_URL_REWRITING = "tomcat60.context.disableURLRewriting";
    private final Logger log;


    public ServerStartupTomcatPropertiesInterceptor(Logger log)
    {
        this.log = log;
    }


    public void actionDuringIteration(Map.Entry<String, String> entry)
    {
        if(((String)entry.getKey()).trim().endsWith("tomcat60.context.disableURLRewriting"))
        {
            this.log.warn("Context.disableURLRewriting has been removed in Tomcat 7 - session-config/tracking-mode elements should be configured in a web application instead. Property '" + (String)entry
                            .getKey() + "' should be removed.");
        }
    }
}
