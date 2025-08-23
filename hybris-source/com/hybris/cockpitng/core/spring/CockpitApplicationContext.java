/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.spring;

import com.hybris.cockpitng.core.CockpitApplicationException;
import com.hybris.cockpitng.core.modules.ModuleInfo;
import com.hybris.cockpitng.core.persistence.packaging.CockpitClassLoader;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;

/**
 * An application context providing CockpitNG features.
 * <P>
 * CockpitNG-based applications should be run using an application context implementing this interface. It also should
 * be a context available under <code>org.springframework.web.context.WebApplicationContext.ROOT</code> attribute in
 * servlet context.
 * </P>
 */
public interface CockpitApplicationContext extends ApplicationContext
{
    /**
     * Extracts the cockpit application context out of the provided one
     *
     * @param applicationContext
     *           base application context
     * @return cockpit application context
     * @throws FatalBeanException
     *            if the cockpit application context cannot be extracted
     */
    static CockpitApplicationContext getCockpitApplicationContext(final ApplicationContext applicationContext)
    {
        if(applicationContext instanceof CockpitApplicationContext)
        {
            return (CockpitApplicationContext)applicationContext;
        }
        else
        {
            LoggerFactory.getLogger(CockpitApplicationContext.class).warn(
                            "Application is not run in the CockpitApplicationContext context - wrapping in DefaultDelegatingCockpitApplicationContext: {}",
                            applicationContext);
            return DefaultDelegatingCockpitApplicationContext.getInstance(applicationContext);
        }
    }


    /**
     * Retrieves a root directory for all the application data
     *
     * @return root directory for application data
     */
    File getDataRootDir();


    /**
     * Retrieves a collection of names all the loaded modules.
     * <P>
     * List should contain only unique values in same order that modules has been loaded.
     * </P>
     *
     * @return names of modules
     */
    List<String> getLoadedModulesNames();


    /**
     * Returns a fetched module name
     *
     * @param moduleURI
     *           URI to module source
     * @return module name
     */
    Optional<String> getModuleName(final URI moduleURI);


    /**
     * Retrieves a URI to source of module
     *
     * @param moduleName
     *           name of module
     * @return source URI
     */
    Optional<URI> getModuleURI(final String moduleName);


    /**
     * Retrieves information about module
     *
     * @param moduleName
     *           module name
     * @return information about module
     * @see #getLoadedModulesNames()
     */
    Optional<ModuleInfo> getModuleInfo(final String moduleName);


    /**
     * Creates a new module library, if it does not exist already. The library is automatically added to application context
     * after consumed.
     *
     * @param moduleName
     *           name of the module for which a library will be registered
     * @param contentsProvider
     *           functional object capable of providing contents for the library
     */
    void registerNewModule(final String moduleName, final ModuleContentProvider contentsProvider)
                    throws CockpitApplicationException;


    /**
     * Removes a module library if such exists. The library is automatically removed from application context.
     *
     * @param moduleName
     *           name of the module for which the library will be unregistered
     */
    void unregisterModule(final String moduleName) throws CockpitApplicationException;


    /**
     * Retrieves a class loader aware of all fetched modules
     *
     * @return class loader
     */
    CockpitClassLoader getClassLoader();


    /**
     * Reloads all modules and beans
     *
     * @throws BeansException
     *            if any problems with context refresh has occurred
     */
    void refresh();


    /**
     * @return
     */
    boolean isReady();
}
