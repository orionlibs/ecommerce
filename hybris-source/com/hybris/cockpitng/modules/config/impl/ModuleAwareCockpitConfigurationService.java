/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.config.impl;

import com.hybris.cockpitng.core.config.impl.DefaultCockpitConfigurationService;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import com.hybris.cockpitng.modules.CockpitModuleConnector;
import com.hybris.cockpitng.modules.server.ws.jaxb.CockpitModuleInfo;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Extension for {@link DefaultCockpitConfigurationService}, so {@link #resetToDefaults()} is aware of cockpit modules
 */
public class ModuleAwareCockpitConfigurationService extends DefaultCockpitConfigurationService implements ApplicationContextAware
{
    private static final Logger LOG = LoggerFactory.getLogger(ModuleAwareCockpitConfigurationService.class);
    private CockpitApplicationContext applicationContext;
    private WidgetLibUtils widgetLibUtils;
    private CockpitModuleConnector cockpitModuleConnector;


    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }


    public static Logger getLOG()
    {
        return LOG;
    }


    public CockpitModuleConnector getCockpitModuleConnector()
    {
        return cockpitModuleConnector;
    }


    public void setCockpitModuleConnector(final CockpitModuleConnector cockpitModuleConnector)
    {
        this.cockpitModuleConnector = cockpitModuleConnector;
    }


    protected CockpitApplicationContext getApplicationContext()
    {
        return applicationContext;
    }


    public void setApplicationContext(final ApplicationContext applicationContext)
    {
        this.applicationContext = CockpitApplicationContext.getCockpitApplicationContext(applicationContext);
    }


    @Override
    protected void resetToDefaultsInternal()
    {
        assureSecure(true);
        Objects.requireNonNull(getApplicationContext().getClassLoader()).reset();
        final String libDirAbsolutePath = widgetLibUtils.libDirAbsolutePath();
        final List<String> modules = cockpitModuleConnector.getCockpitModuleUrls();
        for(int i = 0; i < modules.size(); i++)
        {
            final String moduleUrl = modules.get(i);
            final CockpitModuleInfo cockpitModuleInfo = cockpitModuleConnector.getModuleInfo(moduleUrl);
            if(cockpitModuleInfo != null)
            {
                LOG.info("[{}/{}] CockpitNG loading default configuration for module: {}", i + 1, modules.size(), cockpitModuleInfo.getId());
                cockpitModuleConnector.getLibraryHandler(moduleUrl).afterDeployReverseOrder(cockpitModuleInfo, libDirAbsolutePath);
            }
        }
    }
}
