package de.hybris.platform.media.services.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.media.services.MediaHeadersRegistry;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;

public class DefaultMediaHeadersRegistry implements MediaHeadersRegistry
{
    private static final Logger LOG = Logger.getLogger(DefaultMediaHeadersRegistry.class);
    private static final String LEGACY_HEADER_PROPERTIES_PREFIX = "mediafilter.response.header.";
    private static final String HEADER_PROPERTIES_PREFIX = "media.header.";
    private Map<String, String> headerParams;


    public Map<String, String> getHeaders()
    {
        return this.headerParams;
    }


    @PostConstruct
    void init()
    {
        fillRegistryFromProperties();
        registerConfigChangeListener();
    }


    private void fillRegistryFromProperties()
    {
        if(this.headerParams == null)
        {
            this.headerParams = new ConcurrentHashMap<>();
            ConfigIntf config = getConfig();
            Map<String, String> allParameters = config.getAllParameters();
            for(Map.Entry<String, String> entry : allParameters.entrySet())
            {
                String key = entry.getKey();
                if(isHeaderRelatedProperty(key))
                {
                    String headerKey = extractHeaderFromProperty(key);
                    this.headerParams.put(headerKey, entry.getValue());
                }
            }
        }
    }


    private void registerConfigChangeListener()
    {
        Object object = new Object(this);
        getConfig().registerConfigChangeListener((ConfigIntf.ConfigChangeListener)object);
    }


    ConfigIntf getConfig()
    {
        return Registry.getMasterTenant().getConfig();
    }


    private boolean isHeaderRelatedProperty(String property)
    {
        return (isNewHeaderProperty(property) || isOldHeaderProperty(property));
    }


    private boolean isNewHeaderProperty(String property)
    {
        return (property != null && property.startsWith("media.header."));
    }


    private boolean isOldHeaderProperty(String property)
    {
        return (property != null && property.startsWith("mediafilter.response.header."));
    }


    private String extractHeaderFromProperty(String property)
    {
        String result;
        if(isNewHeaderProperty(property))
        {
            result = property.substring("media.header.".length(), property.length());
        }
        else if(isOldHeaderProperty(property))
        {
            result = property.substring("mediafilter.response.header.".length(), property.length());
        }
        else
        {
            throw new IllegalArgumentException("Not header related property, must start with: media.header.");
        }
        return result;
    }
}
