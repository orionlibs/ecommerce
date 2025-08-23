package de.hybris.platform.media.storage;

import com.google.common.collect.Iterables;
import java.util.Map;

public class DefaultMediaStorageConfig implements MediaStorageConfigService.GlobalMediaStorageConfig
{
    private final Map<String, Object> globalSettings;


    public DefaultMediaStorageConfig(Map<String, Object> globalSettings)
    {
        this.globalSettings = globalSettings;
    }


    public Iterable<String> getKeys()
    {
        return Iterables.unmodifiableIterable(this.globalSettings.keySet());
    }


    public String getParameter(String key)
    {
        return getParameter(key, String.class);
    }


    public <T> T getParameter(String key, Class<T> requiredType)
    {
        Object value = this.globalSettings.get(key);
        if(value != null && !requiredType.isAssignableFrom(value.getClass()))
        {
            throw new IllegalStateException("config value for key: " + key + " is not assignable from " + requiredType
                            .getSimpleName() + " class");
        }
        return (value != null) ? (T)value : null;
    }


    public <T> T getParameter(String key, Class<T> requiredType, T defaultValue)
    {
        T value = getParameter(key, requiredType);
        return (value == null) ? defaultValue : value;
    }


    public String toString()
    {
        return "Global media storage config [settings: " + this.globalSettings + "]";
    }
}
