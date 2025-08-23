package de.hybris.platform.media.storage.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;

public class DefaultMediaFolderConfig implements MediaStorageConfigService.MediaFolderConfig
{
    private final String qualifier;
    private final Map<String, Object> settings;


    public DefaultMediaFolderConfig(String qualifier, Map<String, Object> settings)
    {
        this.settings = settings;
        this.qualifier = qualifier;
    }


    public String getFolderQualifier()
    {
        return this.qualifier;
    }


    public boolean isSecured()
    {
        return ((Boolean)getParameterOrThrowException(DefaultMediaStorageConfigService.DefaultSettingKeys.SECURED_KEY.getKey(), Boolean.class, Boolean.FALSE)).booleanValue();
    }


    public boolean isLocalCacheEnabled()
    {
        return ((Boolean)getParameterOrThrowException(DefaultMediaStorageConfigService.DefaultSettingKeys.LOCAL_CACHE_KEY.getKey(), Boolean.class, Boolean.TRUE))
                        .booleanValue();
    }


    public int getHashingDepth()
    {
        return ((Integer)getParameterOrThrowException(DefaultMediaStorageConfigService.DefaultSettingKeys.HASHING_DEPTH_KEY.getKey(), Integer.class, Integer.valueOf(2)))
                        .intValue();
    }


    public String getStorageStrategyId()
    {
        return getParameterOrThrowException(DefaultMediaStorageConfigService.DefaultSettingKeys.STORAGE_STRATEGY_KEY.getKey(), String.class, null);
    }


    public Iterable<String> getURLStrategyIds()
    {
        return Iterables.unmodifiableIterable(
                        getParameter(DefaultMediaStorageConfigService.DefaultSettingKeys.URL_STRATEGY_KEY.getKey(), Iterable.class, Collections.EMPTY_LIST));
    }


    public boolean isMediaFolderNameValidationEnabled()
    {
        return ((Boolean)getParameterOrThrowException(DefaultMediaStorageConfigService.DefaultSettingKeys.MEDIA_FOLDER_NAME_VALIDATION_KEY.getKey(), Boolean.class, Boolean.valueOf(true))).booleanValue();
    }


    private <T> T getParameterOrThrowException(String key, Class<T> requiredType, T defaultValue)
    {
        T param = (defaultValue != null) ? getParameter(key, requiredType, defaultValue) : getParameter(key, requiredType);
        Preconditions.checkArgument((param != null), "Null value for mandatory key: " + key);
        return param;
    }


    public Iterable<String> getKeys()
    {
        return Iterables.unmodifiableIterable(this.settings.keySet());
    }


    public String getParameter(String key)
    {
        Object value = this.settings.get(key);
        if(value != null)
        {
            return (String)value;
        }
        return null;
    }


    public <T> T getParameter(String key, Class<T> requiredType)
    {
        Object value = this.settings.get(key);
        if(value != null && !requiredType.isAssignableFrom(value.getClass()))
        {
            throw new IllegalStateException("config value for key: " + key + " is not assignable from " + requiredType
                            .getSimpleName() + " class");
        }
        return (value != null) ? (T)value : null;
    }


    public <T> T getParameter(String key, Class<T> requiredType, T defaultValue)
    {
        T param = getParameter(key, requiredType);
        return (param == null) ? defaultValue : param;
    }


    public String toString()
    {
        return
                        MessageFormat.format("Config for: {0} [Storage strategy: {1}, URL Strategy: {2}, Secured: {3}, Local cache: {4}, Hashing depth: {5}, Custom settings: {6}", new Object[] {this.qualifier, getStorageStrategyId(), getURLStrategyIds(), Boolean.valueOf(isSecured()),
                                        Boolean.valueOf(isLocalCacheEnabled()), Integer.valueOf(getHashingDepth()), this.settings});
    }


    public int hashCode()
    {
        return this.qualifier.hashCode();
    }


    public boolean equals(Object obj)
    {
        return (super.equals(obj) || (obj instanceof DefaultMediaFolderConfig && this.settings
                        .equals(((DefaultMediaFolderConfig)obj).settings)));
    }
}
