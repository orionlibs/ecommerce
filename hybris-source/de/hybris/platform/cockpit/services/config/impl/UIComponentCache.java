package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import java.util.HashMap;
import java.util.Map;

public class UIComponentCache
{
    private final Map<CacheKey, UIComponentConfiguration> componentCache = new HashMap<>();


    public <T extends UIComponentConfiguration> T getComponentConfiguration(CacheKey cacheKey)
    {
        return (T)this.componentCache.get(cacheKey);
    }


    public <T extends UIComponentConfiguration> void addComponentConfiguration(CacheKey cacheKey, T componentConfiguration)
    {
        this.componentCache.put(cacheKey, (UIComponentConfiguration)componentConfiguration);
    }


    public void clear()
    {
        this.componentCache.clear();
    }
}
