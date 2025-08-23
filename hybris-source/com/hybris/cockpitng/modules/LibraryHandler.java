/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules;

import com.hybris.cockpitng.core.modules.LibraryFetcher;
import com.hybris.cockpitng.core.modules.ModuleInfo;
import com.hybris.cockpitng.modules.server.ws.jaxb.CockpitModuleInfo;
import java.io.File;

/**
 * Used by {@link com.hybris.cockpitng.modules.impl.AbstractCockpitModuleConnector} to get the cockpit module libraries.
 *
 * @param <INFO>
 *           type of element that describes a module after preparation and allow its proper intialization
 */
public interface LibraryHandler<INFO> extends LibraryFetcher
{
    /**
     * @deprecated since 1808, use {@link LibraryFetcher#canFetchLibrary(ModuleInfo)} instead
     */
    @Override
    @Deprecated(since = "1808", forRemoval = true)
    default boolean canFetchLibrary(final ModuleInfo moduleInfo)
    {
        return true;
    }


    /**
     * @deprecated since 1808, use {@link LibraryFetcher#fetchLibrary(ModuleInfo, File)} instead
     */
    @Deprecated(since = "1808", forRemoval = true)
    void fetchLibrary(CockpitModuleInfo moduleInfo, File archiveFile) throws CockpitModuleDeploymentException;


    /**
     * Called after all cockpit modules have been deployed, in module order, i.e. first module first.
     *
     * @deprecated since 1808, use {@link LibraryFetcher#fetchLibrary(ModuleInfo, File)} instead
     */
    @Deprecated(since = "1808", forRemoval = true)
    void afterDeploy(CockpitModuleInfo moduleInfo, String libDir);


    /**
     * Called after all cockpit modules have been deployed and after {@link #afterDeploy(CockpitModuleInfo, String)} of all
     * modules, in reverse module order, i.e. last module first.
     *
     * @deprecated since 1808, use {@link LibraryFetcher#fetchLibrary(ModuleInfo, File)} instead
     */
    @Deprecated(since = "1808", forRemoval = true)
    void afterDeployReverseOrder(CockpitModuleInfo moduleInfo, String libDir);


    /**
     * Prepares an info object for specified module.
     * <P>
     * Method is called for each module loaded in order of modules loading.
     * </P>
     *
     * @param moduleInfo
     *           information about loaded module
     * @see #initialize(ModuleInfo, Object)
     */
    default INFO prepare(final ModuleInfo moduleInfo)
    {
        if(moduleInfo instanceof CockpitModuleInfo)
        {
            afterDeploy((CockpitModuleInfo)moduleInfo, null);
        }
        return null;
    }


    /**
     * Initializes a module with provided info object.
     * <P>
     * Method is called for each module loaded in reversed order of modules loading.
     * </P>
     *
     * @param moduleInfo
     *           information about loaded module
     * @param info
     *           info object prepared in {@link #prepare(ModuleInfo)} method
     */
    default void initialize(final ModuleInfo moduleInfo, final INFO info)
    {
        if(moduleInfo instanceof CockpitModuleInfo)
        {
            afterDeployReverseOrder((CockpitModuleInfo)moduleInfo, null);
        }
    }
}
