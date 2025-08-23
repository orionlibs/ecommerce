/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.impl;

import com.hybris.cockpitng.core.modules.DefaultModuleInfo;
import com.hybris.cockpitng.core.modules.ModuleInfo;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.modules.CockpitModuleConnector;
import com.hybris.cockpitng.modules.LibraryHandler;
import com.hybris.cockpitng.modules.ModuleEntry;
import com.hybris.cockpitng.modules.server.ws.jaxb.CockpitModuleInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of {@link AbstractCockpitModuleConnector} capable of getting cockpit modules from different locations.
 * Implement the {@link #getModuleInfo(String, boolean)} to fetch the module info from given URL in the extending class.
 */
public abstract class AbstractCockpitModuleConnector implements CockpitModuleConnector
{
    private static final String COCKPITNG_MODULES_CUSTOM_ENABLED = "cockpitng.modules.custom.enabled";
    private CockpitProperties cockpitProperties;
    private List<String> customModuleUrls;
    private List<ModuleEntry> defaultModules;
    private final Map<String, LibraryHandler> libHandlers = new HashMap<>();
    protected final Map<String, CockpitModuleInfo> moduleCache = new ConcurrentHashMap<>();


    /**
     * Implement this method to fetch the module info from given URL in the extending class.
     *
     * @param moduleUrl
     *           URL of the module to fetch
     * @param cached
     *           should return cached one if present.
     * @return module info from given URL
     */
    public abstract CockpitModuleInfo getModuleInfo(final String moduleUrl, final boolean cached);


    @Override
    public boolean isCustomModulesPermitted()
    {
        return Boolean.parseBoolean(getCockpitProperties().getProperty(COCKPITNG_MODULES_CUSTOM_ENABLED));
    }


    @Override
    public List<String> getCockpitModuleUrls()
    {
        final List<String> ret = new ArrayList<>();
        Optional.ofNullable(getDefaultModules())
                        .map(modules -> modules.stream().filter(ModuleEntry::isEnabled).map(ModuleEntry::getUrl).collect(Collectors.toList()))
                        .ifPresent(ret::addAll);
        Optional.ofNullable(getCustomModuleUrls()).ifPresent(ret::addAll);
        return ret;
    }


    @Override
    public String getWidgetTreeContent(final String moduleUrl)
    {
        final CockpitModuleInfo moduleInfo = getModuleInfo(moduleUrl);
        return moduleInfo == null ? null : moduleInfo.getWidgetsExtension();
    }


    @Override
    public CockpitModuleInfo getModuleInfo(final String moduleUrl)
    {
        return getModuleInfo(moduleUrl, true);
    }


    @Override
    public void setCustomModuleUrls(final List<String> customModuleUrls)
    {
        this.customModuleUrls = customModuleUrls;
    }


    @Override
    public List<ModuleEntry> getDefaultModules()
    {
        return defaultModules;
    }


    @Override
    public void setDefaultModules(final List<ModuleEntry> defaultModules)
    {
        this.defaultModules = defaultModules;
    }


    @Override
    public List<String> getCustomModuleUrls()
    {
        return customModuleUrls == null ? Collections.emptyList() : customModuleUrls;
    }


    /**
     * Returns the library handler for the protocol in the given url.<br>
     * Examples: <br>
     * <code>"http://modules.cockpitng.com/somewebapp/rest/cockpitng/module"</code> for remote modules <br>
     * <code>"extension://myBackofficeExt"</code> for hybris extensions with a backoffice module
     */
    @Override
    public LibraryHandler getLibraryHandler(final String uri)
    {
        final String scheme = URI.create(uri).getScheme();
        return StringUtils.isNoneBlank(scheme) ? libHandlers.get(scheme) : null;
    }


    public void setLibraryHandlers(final Map<String, LibraryHandler> libHandlers)
    {
        this.libHandlers.clear();
        this.libHandlers.putAll(libHandlers);
    }


    @Override
    public void updateWidgetsExtension(final ModuleInfo moduleInfo, final String widgetsExtension)
    {
        if(moduleInfo instanceof DefaultModuleInfo)
        {
            ((DefaultModuleInfo)moduleInfo).setWidgetsExtension(widgetsExtension);
        }
    }


    @Override
    public void updateApplicationContextUri(final ModuleInfo moduleInfo, final String contextUri)
    {
        if(moduleInfo instanceof DefaultModuleInfo)
        {
            ((DefaultModuleInfo)moduleInfo).setApplicationContextUri(contextUri);
        }
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
