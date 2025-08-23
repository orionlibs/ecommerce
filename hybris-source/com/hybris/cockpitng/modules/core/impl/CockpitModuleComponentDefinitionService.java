/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.core.impl;

import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.impl.DefaultCockpitComponentDefinitionService;
import com.hybris.cockpitng.core.modules.ModuleInfo;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import com.hybris.cockpitng.modules.CockpitModuleConnector;
import com.hybris.cockpitng.modules.LibraryHandler;
import com.hybris.cockpitng.modules.ServletContextResolver;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Cockpit module aware implementation of {@link CockpitComponentDefinitionService}. Allows to load component
 * definitions from an external module by using {@link CockpitModuleConnector}. Also creates the module application
 * context.
 */
public class CockpitModuleComponentDefinitionService extends DefaultCockpitComponentDefinitionService
                implements CockpitModulesApplicationContextInitializer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CockpitModuleComponentDefinitionService.class);
    private CockpitModuleConnector cockpitModuleConnector;
    private WidgetLibUtils widgetLibUtils;
    private CockpitConfigurationService cockpitConfigurationService;
    private ServletContextResolver servletContextResolver;
    private boolean initialized;


    @Override
    public void initializeCockpitModulesApplicationContext(final ServletContext servletContext)
    {
        onApplicationEvent(new ContextRefreshedEvent(getApplicationContext()));
    }


    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent)
    {
        clearDefinitions();
        if(CockpitApplicationContext.getCockpitApplicationContext(contextRefreshedEvent.getApplicationContext()).isReady())
        {
            fetchExternalWidgets();
            setInitialized(true);
        }
    }


    protected void fetchExternalWidgets()
    {
        final List<String> cockpitModules = getApplicationContext().getLoadedModulesNames();
        LOGGER.info("Initializing loaded modules: {}", cockpitModules);
        final File libdir = new File(widgetLibUtils.getWidgetJarLibDir(), WidgetLibConstants.DEPLOYED_SUBFOLDER_NAME);
        final List<Pair<ModuleInfo, LibraryHandler>> moduleInfoEntries = cockpitModules.stream()
                        .map(getApplicationContext()::getModuleInfo).filter(Optional::isPresent).map(Optional::get)
                        .filter(moduleInfo -> !StringUtils.isBlank(moduleInfo.getWidgetsPackage()))
                        .map(moduleInfo -> Pair.of(moduleInfo, getCockpitModuleConnector().getLibraryHandler(moduleInfo.getLocationUrl())))
                        .collect(Collectors.toList());
        final String libDirString = libdir.toString();
        final List<Triple<ModuleInfo, LibraryHandler, Object>> preparedModuleEntries = doPrepare(moduleInfoEntries, libDirString);
        // Reverse the order of modules and invoke afterDeployReverseOrder
        doInitialize(preparedModuleEntries);
    }


    protected List<Triple<ModuleInfo, LibraryHandler, Object>> doPrepare(
                    final List<Pair<ModuleInfo, LibraryHandler>> moduleInfoEntries, final String libDirString)
    {
        return moduleInfoEntries.stream().filter(entry -> entry.getLeft() != null && entry.getRight() != null)
                        .map(entry -> Triple.of(entry.getLeft(), entry.getRight(), entry.getRight().prepare(entry.getLeft())))
                        .collect(Collectors.toList());
    }


    protected void doInitialize(final List<Triple<ModuleInfo, LibraryHandler, Object>> moduleInfoEntries)
    {
        LOGGER.debug("doInitialize() with delayed write");
        getCockpitConfigurationService().withDelayedWrite(() -> {
            Collections.reverse(moduleInfoEntries);
            moduleInfoEntries.stream().filter(entry -> entry.getLeft() != null && entry.getMiddle() != null)
                            .forEach(entry -> entry.getMiddle().initialize(entry.getLeft(), entry.getRight()));
        });
    }


    @Override
    protected synchronized void loadDefinitions()
    {
        LOGGER.debug("loadDefinitions() with delayed write");
        getCockpitConfigurationService().withDelayedWrite(() -> {
            super.loadDefinitions();
        });
    }


    /**
     * Retrieves original web app context stored as a property and sets it to be the current web app context.
     *
     * @deprecated since 6.7, responsibility moved to {@link com.hybris.cockpitng.core.spring.CockpitApplicationContext}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected ApplicationContext resetOriginalWebAppContext()
    {
        return getApplicationContext();
    }


    /**
     * Sets given application context as current web app context.
     *
     * @param context
     * @deprecated since 6.7, responsibility moved to {@link com.hybris.cockpitng.core.spring.CockpitApplicationContext}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void setWebAppContext(final ApplicationContext context)
    {
        // see javadoc
    }


    protected ServletContext getServletContext()
    {
        ServletContext ctx = null;
        if(this.servletContextResolver != null)
        {
            ctx = this.servletContextResolver.getServletContext();
        }
        return ctx;
    }


    /**
     * @deprecated since 6.7, responsibility moved to {@link com.hybris.cockpitng.core.spring.CockpitApplicationContext}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected ClassLoader getExternalModuleClassLoader()
    {
        return getApplicationContext().getClassLoader();
    }


    /**
     * @deprecated since 6.7, responsibility moved to {@link com.hybris.cockpitng.core.spring.CockpitApplicationContext}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public synchronized ApplicationContext getExternalApplicationContext()
    {
        return getApplicationContext();
    }


    protected CockpitModuleConnector getCockpitModuleConnector()
    {
        return cockpitModuleConnector;
    }


    @Required
    public void setCockpitModuleConnector(final CockpitModuleConnector cockpitModuleConnector)
    {
        this.cockpitModuleConnector = cockpitModuleConnector;
    }


    @Required
    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }


    public boolean isInitialized()
    {
        return initialized;
    }


    public void setInitialized(final boolean initialized)
    {
        this.initialized = initialized;
    }


    /**
     * @deprecated since 6.7, responsibility moved to {@link com.hybris.cockpitng.core.spring.CockpitApplicationContext}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected boolean isForceModuleCtxEnabled()
    {
        return false;
    }


    protected ServletContextResolver getServletContextResolver()
    {
        return servletContextResolver;
    }


    public void setServletContextResolver(final ServletContextResolver servletContextResolver)
    {
        this.servletContextResolver = servletContextResolver;
    }


    /**
     * Clear modules jar classloader.
     */
    public synchronized void clearModuleClassLoader()
    {
        getDefinitionLoaders().stream().filter(loader -> loader instanceof ModuleJarDefinitionLoader)
                        .forEach(loader -> ((ModuleJarDefinitionLoader)loader).clearModuleClassLoader());
    }


    /**
     * @deprecated since 6.7, responsibility moved to {@link com.hybris.cockpitng.core.spring.CockpitApplicationContext}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public void setCleanLibDirOnReload(final boolean cleanLibDirOnReload)
    {
        // see javadoc
    }


    protected CockpitConfigurationService getCockpitConfigurationService()
    {
        return cockpitConfigurationService;
    }


    @Required
    public void setCockpitConfigurationService(final CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }
}
