package de.hybris.platform.media.storage.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import de.hybris.platform.core.Registry;
import de.hybris.platform.media.storage.ConfigValueConverter;
import de.hybris.platform.media.storage.DefaultMediaStorageConfig;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.regioncache.ConcurrentHashSet;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DefaultMediaStorageConfigService implements MediaStorageConfigService
{
    private static final Logger LOG = Logger.getLogger(DefaultMediaStorageConfigService.class);
    private static final String MEDIA_FOLDER_PREFIX = "media.folder.";
    private static final String MEDIA_DEFAULT_PREFIX = "media.default.";
    private static final String MEDIA_GLOBAL_SETTINGS_PREFIX = "media.globalSettings.";
    private final Pattern globalSettingsPattern = Pattern.compile("media.globalSettings\\.(\\w+)\\.(.+)");
    private final Pattern folderSettingsPattern = Pattern.compile("media.folder\\.([\\w\\-]+)\\.(.+)");
    private final Pattern defaultSettingsPattern = Pattern.compile("media.default\\.(\\w+)\\.(.+)");
    private final ConfigValueMappingRegistry valueMappingRegistry;
    private Map<String, Map<String, Object>> globalSettings;
    private Map<String, Map<String, Object>> foldersSettings;
    private Map<String, Object> defaultSettings;
    private Set<MediaStorageConfigService.MediaFolderConfig> foldersConfigs = (Set<MediaStorageConfigService.MediaFolderConfig>)new ConcurrentHashSet();
    private MediaStorageConfigService.GlobalMediaStorageConfig defaultStorageStrategyConfig;
    private Collection<String> securedFolders;
    private final ConfigIntf.ConfigChangeListener configChangeListener = (ConfigIntf.ConfigChangeListener)new Object(this);


    public DefaultMediaStorageConfigService(ConfigValueMappingRegistry valueMappingRegistry)
    {
        this.valueMappingRegistry = valueMappingRegistry;
    }


    @PostConstruct
    public void init()
    {
        this.defaultSettings = filterAndConvertDefaultSettings();
        this.globalSettings = filterAndConvertSettingsWithPrefix("media.globalSettings.", this.globalSettingsPattern);
        this.foldersSettings = filterAndConvertSettingsWithPrefix("media.folder.", this.folderSettingsPattern);
        this.foldersConfigs = buildConfigForFolders();
        this.defaultStorageStrategyConfig = buildConfigForDefaultStorageStrategy();
        this.securedFolders = new HashSet<>(getSecuredFoldersFromProperties());
        registerConfigChangeListener();
    }


    Set<String> getSecuredFoldersFromProperties()
    {
        ConfigIntf config = Registry.getCurrentTenantNoFallback().getConfig();
        Map<String, String> allFolders = config.getParametersMatching("media\\.folder\\.(.*)\\.secured", true);
        return (Set<String>)allFolders.entrySet().stream().filter(e -> "true".equalsIgnoreCase((String)e.getValue())).map(Map.Entry::getKey)
                        .collect(Collectors.toSet());
    }


    private Map<String, Object> filterAndConvertDefaultSettings()
    {
        DefaultSettingKeys[] defaultSettingsKeys = DefaultSettingKeys.values();
        Map<String, Object> result = new HashMap<>(defaultSettingsKeys.length);
        Map<String, String> mediaDefaultSettings = getPropertiesWithPrefix("media.default.");
        for(DefaultSettingKeys defaultSetting : defaultSettingsKeys)
        {
            String value = mediaDefaultSettings.get("media.default." + defaultSetting.getKey());
            fillDefaultSettingsWithValues(result, defaultSetting.getKey(), value);
        }
        return result;
    }


    private void fillDefaultSettingsWithValues(Map<String, Object> defaultSettings, String key, String value)
    {
        if(StringUtils.isNotBlank(value))
        {
            ConfigValueConverter converter = (ConfigValueConverter)this.valueMappingRegistry.getMappings().get(key);
            Object configValue = (converter != null) ? converter.convert(value) : value;
            defaultSettings.put(key, configValue);
        }
        else
        {
            throw new IllegalStateException("Incorrect media storage configuration - mandatory default setting not present [key: " + key + "]");
        }
    }


    private Map<String, Map<String, Object>> filterAndConvertSettingsWithPrefix(String propertyPrefix, Pattern keyPattern)
    {
        Map<String, Map<String, Object>> result = new HashMap<>();
        Map<String, String> props = getPropertiesWithPrefix(propertyPrefix);
        for(Map.Entry<String, String> entry : props.entrySet())
        {
            Matcher matcher = keyPattern.matcher(entry.getKey());
            if(StringUtils.isNotBlank(entry.getValue()) && matcher.matches())
            {
                ConfigEntryHolder configEntryHolder = buildConfigEntryHolder(entry.getValue(), matcher);
                fillSettingsWithValues(result, configEntryHolder.getMainKey(), configEntryHolder.getSettingKey(), configEntryHolder
                                .getValue());
            }
        }
        return result;
    }


    private Set<MediaStorageConfigService.MediaFolderConfig> buildConfigForFolders()
    {
        ConcurrentHashSet<MediaStorageConfigService.MediaFolderConfig> concurrentHashSet = new ConcurrentHashSet();
        for(Map.Entry<String, Map<String, Object>> entry : this.foldersSettings.entrySet())
        {
            concurrentHashSet.add(buildConfigForFolder(entry.getKey()));
        }
        return (Set<MediaStorageConfigService.MediaFolderConfig>)concurrentHashSet;
    }


    void registerConfigChangeListener()
    {
        Registry.getCurrentTenant().getConfig().registerConfigChangeListener(this.configChangeListener);
    }


    private MediaStorageConfigService.MediaFolderConfig buildConfigForFolder(String folderQualifier)
    {
        String strategyId = getValueFromFolderOrDefaultSettings(folderQualifier, DefaultSettingKeys.STORAGE_STRATEGY_KEY
                        .getKey(), String.class);
        return (MediaStorageConfigService.MediaFolderConfig)new DefaultMediaFolderConfig(folderQualifier, getFolderSettings(folderQualifier, strategyId));
    }


    private Map<String, Object> getFolderSettings(String folderQualifier, String strategyId)
    {
        Map<String, Object> settings = getDefaultSettingsForFolder(folderQualifier, strategyId);
        Map<String, Object> customSettings = getCustomSettingsFromFolderOrGlobalSettings(folderQualifier, strategyId);
        settings.putAll(customSettings);
        return MapUtils.unmodifiableMap(settings);
    }


    private Map<String, Object> getDefaultSettingsForFolder(String folderQualifier, String strategyId)
    {
        Iterable<String> urlStrategies = getValueFromFolderOrDefaultSettings(folderQualifier, DefaultSettingKeys.URL_STRATEGY_KEY
                        .getKey(), (Class)Iterable.class);
        Boolean secured = getValueFromFolderOrDefaultSettings(folderQualifier, DefaultSettingKeys.SECURED_KEY.getKey(), Boolean.class);
        Boolean localCacheEnabled = getValueFromFolderOrDefaultSettings(folderQualifier, DefaultSettingKeys.LOCAL_CACHE_KEY
                        .getKey(), Boolean.class);
        Integer hashingDepth = getValueFromFolderOrDefaultSettings(folderQualifier, DefaultSettingKeys.HASHING_DEPTH_KEY
                        .getKey(), Integer.class);
        Boolean mediaFolderNameValidationEnabled = getValueFromFolderOrDefaultSettings(folderQualifier, DefaultSettingKeys.MEDIA_FOLDER_NAME_VALIDATION_KEY
                        .getKey(), Boolean.class);
        Map<String, Object> defaultSettings = new HashMap<>();
        defaultSettings.put(DefaultSettingKeys.STORAGE_STRATEGY_KEY.getKey(), strategyId);
        defaultSettings.put(DefaultSettingKeys.URL_STRATEGY_KEY.getKey(), urlStrategies);
        defaultSettings.put(DefaultSettingKeys.SECURED_KEY.getKey(), secured);
        defaultSettings.put(DefaultSettingKeys.LOCAL_CACHE_KEY.getKey(), localCacheEnabled);
        defaultSettings.put(DefaultSettingKeys.HASHING_DEPTH_KEY.getKey(), hashingDepth);
        defaultSettings.put(DefaultSettingKeys.MEDIA_FOLDER_NAME_VALIDATION_KEY.getKey(), mediaFolderNameValidationEnabled);
        return defaultSettings;
    }


    public boolean isStorageStrategyConfigured(String strategyId)
    {
        Set<MediaStorageConfigService.MediaFolderConfig> configs = getFolderConfigsForStrategy(strategyId);
        String globalStrategy = getValueFromDefaultSettings(DefaultSettingKeys.STORAGE_STRATEGY_KEY.getKey(), String.class);
        return ((configs != null && !configs.isEmpty()) || (
                        StringUtils.isNotBlank(globalStrategy) && globalStrategy.equalsIgnoreCase(strategyId)));
    }


    public Collection<String> getSecuredFolders()
    {
        return this.securedFolders;
    }


    public MediaStorageConfigService.MediaFolderConfig getConfigForFolder(String folderQualifier)
    {
        MediaStorageConfigService.MediaFolderConfig config;
        Preconditions.checkArgument((folderQualifier != null), "folderQualifier is required");
        Optional<MediaStorageConfigService.MediaFolderConfig> folderConfig = Iterables.tryFind(this.foldersConfigs, (Predicate)new Object(this, folderQualifier));
        if(folderConfig.isPresent())
        {
            config = (MediaStorageConfigService.MediaFolderConfig)folderConfig.get();
        }
        else
        {
            config = buildConfigForFolder(folderQualifier);
            this.foldersConfigs.add(config);
        }
        return config;
    }


    private <T> T getValueFromFolderOrDefaultSettings(String folderQualifier, String settingKey, Class<T> requiredClass)
    {
        Object value;
        Map<String, Object> folderSettings = this.foldersSettings.get(folderQualifier);
        if(folderSettings == null)
        {
            value = this.defaultSettings.get(settingKey);
        }
        else
        {
            value = folderSettings.get(settingKey);
            if(value == null)
            {
                value = this.defaultSettings.get(settingKey);
            }
        }
        if(value != null && !requiredClass.isAssignableFrom(value.getClass()))
        {
            throw new IllegalStateException("config value for key: " + settingKey + " is not assignable from " + requiredClass
                            .getSimpleName() + " class");
        }
        return (T)value;
    }


    private Map<String, Object> getCustomSettingsFromFolderOrGlobalSettings(String folderQualifier, String strategyId)
    {
        Map<String, Object> customSettings = new HashMap<>();
        Map<String, Object> globalSettingsForStrategy = this.globalSettings.get(strategyId);
        if(globalSettingsForStrategy != null)
        {
            customSettings.putAll(globalSettingsForStrategy);
        }
        Map<String, Object> settings = this.foldersSettings.get(folderQualifier);
        if(settings != null)
        {
            for(Map.Entry<String, Object> entry : settings.entrySet())
            {
                customSettings.put(entry.getKey(), entry.getValue());
            }
        }
        return customSettings;
    }


    private MediaStorageConfigService.GlobalMediaStorageConfig buildConfigForDefaultStorageStrategy()
    {
        String strategyId = (String)this.defaultSettings.get(DefaultSettingKeys.STORAGE_STRATEGY_KEY.getKey());
        Map<String, Object> settings = this.globalSettings.get(strategyId);
        return (MediaStorageConfigService.GlobalMediaStorageConfig)new DefaultMediaStorageConfig((settings == null) ? Collections.EMPTY_MAP : MapUtils.unmodifiableMap(settings));
    }


    public String getDefaultStrategyId()
    {
        return getValueFromDefaultSettings(DefaultSettingKeys.STORAGE_STRATEGY_KEY.getKey(), String.class);
    }


    public Set<MediaStorageConfigService.MediaFolderConfig> getFolderConfigsForStrategy(String strategyId)
    {
        Preconditions.checkArgument((strategyId != null), "strategyId is required");
        Iterable<MediaStorageConfigService.MediaFolderConfig> filtered = Iterables.filter(this.foldersConfigs, (Predicate)new Object(this, strategyId));
        return (filtered == null || Iterables.isEmpty(filtered)) ? Collections.EMPTY_SET : (Set<MediaStorageConfigService.MediaFolderConfig>)ImmutableSet.builder()
                        .addAll(filtered).build();
    }


    public MediaStorageConfigService.GlobalMediaStorageConfig getGlobalSettingsForStrategy(String strategyId)
    {
        return this.defaultStorageStrategyConfig;
    }


    public String getDefaultCacheFolderName()
    {
        return getValueFromDefaultSettings(DefaultSettingKeys.LOCAL_CACHE_ROOT_FOLDER_KEY.getKey(), String.class);
    }


    private void fillSettingsWithValues(Map<String, Map<String, Object>> result, String mainKey, String settingKey, Object value)
    {
        Map<String, Object> settings = result.get(mainKey);
        if(settings == null)
        {
            settings = new HashMap<>();
            result.put(mainKey, settings);
        }
        settings.put(settingKey, value);
    }


    private ConfigEntryHolder buildConfigEntryHolder(String propertyValue, Matcher matcher)
    {
        String mainKey = matcher.group(1);
        String settingKey = matcher.group(2);
        ConfigValueConverter converter = (ConfigValueConverter)this.valueMappingRegistry.getMappings().get(settingKey);
        Object value = (converter != null && propertyValue != null) ? converter.convert(propertyValue) : propertyValue;
        return new ConfigEntryHolder(this, mainKey, settingKey, value);
    }


    Map<String, String> getPropertiesWithPrefix(String prefix)
    {
        return Config.getParametersByPattern(prefix);
    }


    private <T> T getValueFromDefaultSettings(String key, Class<T> requiredClass)
    {
        Object result = this.defaultSettings.get(key);
        if(result != null)
        {
            Preconditions.checkArgument(requiredClass.isAssignableFrom(result.getClass()), "value is not assignable from " + requiredClass
                            .getName());
            return (T)result;
        }
        return null;
    }
}
