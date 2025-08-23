package de.hybris.tomcat;

import org.apache.tomcat.util.IntrospectionUtils;

public class EnvPropertySource implements IntrospectionUtils.PropertySource
{
    private static final String PREFIX = "ENV::";


    public String getProperty(String key)
    {
        if(key == null || !key.startsWith("ENV::"))
        {
            return null;
        }
        String envName = key.substring("ENV::".length());
        if(envName.isEmpty())
        {
            return null;
        }
        return System.getenv(envName);
    }
}
