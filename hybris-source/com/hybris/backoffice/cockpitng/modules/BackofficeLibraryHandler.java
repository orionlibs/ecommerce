/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.modules;

import com.hybris.cockpitng.core.CockpitApplicationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultCockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import com.hybris.cockpitng.core.modules.ModuleInfo;
import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.jaxb.SchemaValidationStatus;
import com.hybris.cockpitng.modules.CockpitModuleConnector;
import com.hybris.cockpitng.modules.CockpitModuleDeploymentException;
import com.hybris.cockpitng.modules.LibraryHandler;
import com.hybris.cockpitng.modules.server.ws.jaxb.CockpitModuleInfo;
import de.hybris.platform.regioncache.ConcurrentHashSet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Implementation of {@link LibraryHandler} for hybris platform, responsible for getting the backoffice module extension
 * libs.
 */
public class BackofficeLibraryHandler extends BackofficeLibraryFetcher implements LibraryHandler<Object>, ApplicationContextAware
{
    public static final String CONFIG_CONTEXT_MODULE = "module";
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeLibraryHandler.class);
    private static final String VALIDATE_COCKPIT_CONFIG_ON_STARTUP_PROPERTY = "cockpitng.validate.cockpitConfig.onstartup";
    private DefaultCockpitConfigurationService cockpitConfigurationService;
    private CockpitProperties cockpitProperties;
    private CockpitApplicationContext applicationContext;
    private CockpitModuleConnector cockpitModuleConnector;
    private Set<String> initializedModules = new ConcurrentHashSet<>();


    @Override
    public void setApplicationContext(final ApplicationContext applicationContext)
    {
        this.applicationContext = CockpitApplicationContext.getCockpitApplicationContext(applicationContext);
    }


    protected CockpitApplicationContext getApplicationContext()
    {
        return applicationContext;
    }


    @Override
    public void fetchLibrary(final CockpitModuleInfo moduleInfo, final File archiveFile) throws CockpitModuleDeploymentException
    {
        try
        {
            fetchLibrary((ModuleInfo)moduleInfo, archiveFile);
        }
        catch(final CockpitModuleDeploymentException e)
        {
            throw e;
        }
        catch(final CockpitApplicationException e)
        {
            throw new CockpitModuleDeploymentException(e);
        }
    }


    @Override
    public Object prepare(final ModuleInfo moduleInfo)
    {
        final String extensionName = moduleInfo.getId();
        if(extensionName == null)
        {
            throw new IllegalArgumentException("Module info needs to define its identity");
        }
        final ClassLoader classLoader = getApplicationContext().getClassLoader();
        // set up spring configuration file location
        final String resourceFileName = BackofficeFileConventionUtils.getModuleSpringDefinitionsFile(extensionName);
        // NOTE: using getResourceAsStream to check if the resource exists - instead of getResource() -
        // because the cockpit widget loader does not handle it yet.
        final InputStream inputStream = classLoader.getResourceAsStream(resourceFileName);
        if(inputStream != null)
        {
            IOUtils.closeQuietly(inputStream);
            getCockpitModuleConnector().updateApplicationContextUri(moduleInfo, "classpath:" + resourceFileName);
        }
        // load widgets xml
        String widgetsString = "";
        final String widgetsFileName = BackofficeFileConventionUtils.getModuleWidgetsXmlFile(extensionName);
        InputStream resourceAsStream = null;
        try
        {
            resourceAsStream = classLoader.getResourceAsStream(widgetsFileName);
            if(resourceAsStream != null)
            {
                try
                {
                    widgetsString = IOUtils.toString(resourceAsStream);
                }
                catch(final IOException e)
                {
                    LOG.error(String.format("Could not read widget config for extension %s", extensionName), e);
                }
                getCockpitModuleConnector().updateWidgetsExtension(moduleInfo, widgetsString);
            }
        }
        finally
        {
            IOUtils.closeQuietly(resourceAsStream);
        }
        return null;
    }


    @Override
    public void initialize(final ModuleInfo moduleInfo, final Object o)
    {
        // add default config snippet
        final String extensionName = moduleInfo.getId();
        if(extensionName == null)
        {
            return;
        }
        if(initializedModules.contains(extensionName))
        {
            LOG.debug("Ignored, {} has been initialized!", extensionName);
            return;
        }
        final ClassLoader classLoader = getApplicationContext().getClassLoader();
        if(classLoader == null)
        {
            return;
        }
        final String configResourceFileName = BackofficeFileConventionUtils.getModuleConfigXmlFile(extensionName);
        if(shouldValidateCockpitConfigOnStartup() && !validateCockpitConfigOnStartup(classLoader, configResourceFileName))
        {
            return;
        }
        try
        {
            InputStream stream = null;
            try
            {
                stream = classLoader.getResourceAsStream(configResourceFileName);
                if(stream != null)
                {
                    final Config rootConfig = this.cockpitConfigurationService.loadRootConfiguration(stream);
                    if(rootConfig != null)
                    {
                        addModuleContext(moduleInfo.getId(), rootConfig);
                        final Config mainRootConfig = this.cockpitConfigurationService.loadRootConfiguration();
                        if(initializedModules.contains(extensionName))
                        {
                            LOG.debug("Ignored, {} has been initialized!", extensionName);
                            return;
                        }
                        LOG.debug("Load module config from {}, context size: {}, default context size: {}",
                                        configResourceFileName,
                                        rootConfig.getContext().size(),
                                        mainRootConfig.getContext().size());
                        final boolean updated = updateMainConfig(mainRootConfig, rootConfig);
                        if(updated)
                        {
                            LOG.debug("{} is different from default config, need to be updated.", configResourceFileName);
                            this.cockpitConfigurationService.storeRootConfig(mainRootConfig);
                            initializedModules.add(extensionName);
                        }
                    }
                }
            }
            finally
            {
                IOUtils.closeQuietly(stream);
                LOG.debug("{} initialized!", extensionName);
            }
        }
        catch(final CockpitConfigurationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Exception thrown: ", e);
            }
        }
    }


    protected SchemaValidationStatus validateCockpitConfiguration(final ClassLoader classLoader, final String configuration)
    {
        InputStream stream = null;
        try
        {
            stream = classLoader.getResourceAsStream(configuration);
            if(stream != null)
            {
                return this.cockpitConfigurationService.validate(stream);
            }
            else
            {
                return SchemaValidationStatus.error();
            }
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }
    }


    private boolean shouldValidateCockpitConfigOnStartup()
    {
        return cockpitProperties != null && cockpitProperties.getBoolean(VALIDATE_COCKPIT_CONFIG_ON_STARTUP_PROPERTY);
    }


    private void addModuleContext(final String moduleName, final Config rootConfig)
    {
        for(final Context context : rootConfig.getContext())
        {
            addModuleContext(moduleName, context);
        }
    }


    private boolean validateCockpitConfigOnStartup(ClassLoader classLoader, String configResourceFileName)
    {
        Boolean validateResult = true;
        final SchemaValidationStatus validationStatus = validateCockpitConfiguration(classLoader, configResourceFileName);
        if(validationStatus.isError())
        {
            LOG.error("{} could not be validated and may not be merged. Check previous messages for details.", configResourceFileName);
            validateResult = false;
        }
        else if(validationStatus.isWarning())
        {
            LOG.warn("Validation of {} returned warnings. Check previous messages for details.", configResourceFileName);
        }
        return validateResult;
    }


    private void addModuleContext(final String moduleName, final Context context)
    {
        final Map<String, String> ctx = this.cockpitConfigurationService.getContext(context);
        ctx.put(CONFIG_CONTEXT_MODULE, moduleName);
        this.cockpitConfigurationService.setContext(context, ctx);
        for(final Context child : context.getContext())
        {
            addModuleContext(moduleName, child);
        }
    }


    private boolean updateMainConfig(final Config mainRootConfig, final Config rootConfig)
    {
        boolean updated = false;
        for(final Context context : rootConfig.getContext())
        {
            updated |= updateMainConfig(mainRootConfig, context);
        }
        return updated;
    }


    private boolean updateMainConfig(final Config mainRootConfig, final Context context)
    {
        boolean updated = false;
        final Object element = context.getAny();
        if(element != null)
        {
            final Map<String, String> ctx = this.cockpitConfigurationService.getContext(context);
            Context mainContext = null;
            final List<Context> mainContextList = findContext(mainRootConfig, ctx);
            if(mainContextList == null || mainContextList.isEmpty())
            {
                final String mergeBy = getFirstNotNull(context, Context::getMergeBy);
                final String parent = Optional.ofNullable(getFirstNotNull(context, Context::getParent))
                                .filter(((Predicate<String>)"auto"::equals).negate()).orElse(null);
                mainContext = new Context();
                mainContext.setMergeBy(mergeBy);
                mainContext.setParent(parent);
                this.cockpitConfigurationService.setContext(mainContext, ctx);
                mainRootConfig.getContext().add(mainContext);
            }
            else
            {
                final Context lastOne = mainContextList.get(mainContextList.size() - 1);
                if(lastOne.getAny() == null)
                {
                    mainContext = lastOne;
                }
            }
            if(mainContext != null && ObjectUtils.notEqual(mainContext.getAny(), element))
            {
                mainContext.setAny(element);
                updated = true;
            }
        }
        for(final Context child : context.getContext())
        {
            updated |= updateMainConfig(mainRootConfig, child);
        }
        return updated;
    }


    private List<Context> findContext(final Config mainRootConfig, final Map<String, String> ctx)
    {
        final List<Context> mainContextList = this.cockpitConfigurationService.findContext(mainRootConfig, ctx, false, true);
        if(CollectionUtils.isNotEmpty(mainContextList))
        {
            final List<Context> result = new ArrayList<>(mainContextList);
            for(final Context context : mainContextList)
            {
                final Map<String, String> ctx2 = this.cockpitConfigurationService.getContext(context);
                if(!ctx.equals(ctx2))
                {
                    result.remove(context);
                }
            }
            return result;
        }
        return Collections.emptyList();
    }


    private String getFirstNotNull(final Context context, final Function<Context, String> valueSupplier)
    {
        if(context == null)
        {
            return null;
        }
        else
        {
            return Optional.of(context).map(valueSupplier)
                            .orElseGet(() -> getFirstNotNull(context.getParentContext(), valueSupplier));
        }
    }


    protected DefaultCockpitConfigurationService getCockpitConfigurationService()
    {
        return cockpitConfigurationService;
    }


    @Required
    public void setCockpitConfigurationService(final DefaultCockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
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


    protected CockpitModuleConnector getCockpitModuleConnector()
    {
        return cockpitModuleConnector;
    }


    @Required
    public void setCockpitModuleConnector(final CockpitModuleConnector cockpitModuleConnector)
    {
        this.cockpitModuleConnector = cockpitModuleConnector;
    }


    @Override
    public void afterDeploy(final CockpitModuleInfo moduleInfo, final String libDir)
    {
        prepare(moduleInfo);
    }


    @Override
    public void afterDeployReverseOrder(final CockpitModuleInfo moduleInfo, final String libDir)
    {
        LOG.debug("Remove {} from initializedModules!", moduleInfo.getId());
        initializedModules.remove(moduleInfo.getId());
        initialize(moduleInfo, null);
    }
}
