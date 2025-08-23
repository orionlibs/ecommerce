package de.hybris.platform.servicelayer.config.impl;

import com.google.common.collect.Maps;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationViewService;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

public class SecureConfigurationViewService implements ConfigurationViewService
{
    public static final String CONFIGURATION_VIEW_STRATEGY = "configuration.view.strategy";
    public static final String BLACKLIST_EXACT_MATCH = "configuration\\.view\\.blacklist\\.(.*)";
    public static final String BLACKLIST_REGEX_RULE = "configuration\\.view\\.regex\\.rule\\.(.*)";
    public static final String REMOVE_STRATEGY = "remove";
    private static final String HIDDEN_VALUE = "*****";
    private final boolean removeBlacklistedProperties;
    private final Supplier<Map<String, String>> envSupplier;
    private final Supplier<Properties> propertiesSupplier;
    private final Supplier<ConfigIntf> configSupplier;
    private final Set<String> blacklist = new HashSet<>();
    private final Set<String> envPropertiesBlacklist;
    private final List<Pattern> blacklistPatterns = new ArrayList<>();


    public SecureConfigurationViewService()
    {
        this(() -> System.getenv(), () -> System.getProperties(), () -> Registry.getCurrentTenant().getConfig());
    }


    public SecureConfigurationViewService(Supplier<Map<String, String>> envSupplier, Supplier<Properties> propertiesSupplier, Supplier<ConfigIntf> configSupplier)
    {
        this.envSupplier = envSupplier;
        this.propertiesSupplier = propertiesSupplier;
        this.configSupplier = configSupplier;
        Map<String, String> exactBlacklistConfig = ((ConfigIntf)configSupplier.get()).getParametersMatching("configuration\\.view\\.blacklist\\.(.*)", true);
        Map<String, String> blacklistRegexRulesConfig = ((ConfigIntf)configSupplier.get()).getParametersMatching("configuration\\.view\\.regex\\.rule\\.(.*)", true);
        for(String blacklistEntry : exactBlacklistConfig.values())
        {
            List<String> blacklistEntries = Arrays.asList(blacklistEntry.split(","));
            this.blacklist.addAll(canonize(blacklistEntries));
        }
        for(String regex : blacklistRegexRulesConfig.values())
        {
            if(StringUtils.isNotBlank(regex))
            {
                this.blacklistPatterns.add(Pattern.compile(regex, 2));
            }
        }
        this.removeBlacklistedProperties = shouldRemoveBlacklistedProperties();
        this.envPropertiesBlacklist = prepareEnvBlacklist(this.blacklist);
    }


    private Set<String> canonize(List<String> blacklistEntries)
    {
        return (Set<String>)blacklistEntries.stream().map(i -> i.toLowerCase()).collect(Collectors.toSet());
    }


    private Set<String> prepareEnvBlacklist(Set<String> blacklist)
    {
        Set<String> blacklistEnvNames = new HashSet<>();
        for(String entry : blacklist)
        {
            String adjusted = getEnvVariablesPrefix() + getEnvVariablesPrefix();
            blacklistEnvNames.add(adjusted);
        }
        return blacklistEnvNames;
    }


    private String getEnvVariablesPrefix()
    {
        return ((ConfigIntf)this.configSupplier.get()).getString("env.properties.prefix", "y_");
    }


    private boolean shouldRemoveBlacklistedProperties()
    {
        String configurationStrategy = ((ConfigIntf)this.configSupplier.get()).getString("configuration.view.strategy", "remove");
        return "remove".equalsIgnoreCase(configurationStrategy);
    }


    public Properties readSystemProperties()
    {
        Properties systemProperties = this.propertiesSupplier.get();
        Map<String, String> stringStringMap = transformBlacklistedProperties((Map<String, String>)Maps.fromProperties(systemProperties));
        Properties blacklistedProperties = new Properties();
        blacklistedProperties.putAll(stringStringMap);
        return blacklistedProperties;
    }


    public Map<String, String> readEnvVariables()
    {
        Map<String, String> envVariables = this.envSupplier.get();
        return transformBlacklistedProperties(envVariables);
    }


    public Map<String, String> readConfigParameters()
    {
        Map<String, String> allParameters = ((ConfigIntf)this.configSupplier.get()).getAllParameters();
        return transformBlacklistedProperties(allParameters);
    }


    private Map<String, String> transformBlacklistedProperties(Map<String, String> allParameters)
    {
        if(this.removeBlacklistedProperties)
        {
            return removeBlacklistedProperties(allParameters);
        }
        return hideBlacklistedProperties(allParameters);
    }


    private Map<String, String> removeBlacklistedProperties(Map<String, String> allParameters)
    {
        return (Map<String, String>)allParameters.entrySet().stream()
                        .filter(i -> !isBlacklistedByRules(((String)i.getKey()).toLowerCase()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    private Map<String, String> hideBlacklistedProperties(Map<String, String> inputParameters)
    {
        Map<String, String> parameters = new HashMap<>(inputParameters);
        for(Map.Entry<String, String> entry : parameters.entrySet())
        {
            if(isBlacklistedByRules(((String)entry.getKey()).toLowerCase()))
            {
                entry.setValue("*****");
            }
        }
        return parameters;
    }


    private boolean isBlacklistedByRules(String propertyName)
    {
        if(this.blacklist.contains(propertyName) || this.envPropertiesBlacklist.contains(propertyName))
        {
            return true;
        }
        for(Pattern pattern : this.blacklistPatterns)
        {
            if(pattern.matcher(propertyName).find())
            {
                return true;
            }
            String prefix = getEnvVariablesPrefix();
            if(propertyName.startsWith(prefix))
            {
                String adjusted = propertyName.substring(prefix.length()).replace("_", ".");
                if(pattern.matcher(adjusted).find())
                {
                    return true;
                }
            }
        }
        return false;
    }
}
