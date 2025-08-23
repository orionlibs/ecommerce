package com.hybris.backoffice.search.setup.impl;

import com.hybris.backoffice.search.setup.BackofficeSearchSystemSetupConfig;
import de.hybris.platform.core.Registry;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AbstractBackofficeSearchSystemSetupConfig implements BackofficeSearchSystemSetupConfig
{
    private static final String DEFAULT_FILE_LIST_SEPARATOR = ",";
    private static final String DEFAULT_FILE_ENCODING = "UTF-8";
    private static final String DEFAULT_LANGUAGE_SEPARATOR = "_";
    private final ConfigStringResolver configStringResolver;


    public AbstractBackofficeSearchSystemSetupConfig(ConfigStringResolver resolver)
    {
        this.configStringResolver = resolver;
    }


    public Collection<String> getLocalizedRootNames()
    {
        return Collections.emptyList();
    }


    public Collection<String> getNonLocalizedRootNames()
    {
        return Collections.emptyList();
    }


    public String getConfigurationRoots(String configuredRootsKey)
    {
        return getConfigStringResolver().resolveConfigStringParameter(configuredRootsKey);
    }


    public String getFileEncoding()
    {
        return "UTF-8";
    }


    public String getRootNameLanguageSeparator()
    {
        return "_";
    }


    public String getListSeparator()
    {
        return ",";
    }


    public boolean isExtensionLoaded(String extensionNameToCheck)
    {
        List<String> loadedExtensionNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();
        return loadedExtensionNames.contains(extensionNameToCheck);
    }


    protected ConfigStringResolver getConfigStringResolver()
    {
        return this.configStringResolver;
    }
}
