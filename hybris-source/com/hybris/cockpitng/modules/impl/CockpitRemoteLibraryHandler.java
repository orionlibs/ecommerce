/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.impl;

import com.hybris.cockpitng.core.modules.LibraryFetcher;
import com.hybris.cockpitng.core.modules.ModuleInfo;
import com.hybris.cockpitng.modules.CockpitModuleDeploymentException;
import com.hybris.cockpitng.modules.LibraryHandler;
import com.hybris.cockpitng.modules.server.ws.jaxb.CockpitModuleInfo;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LibraryHandler capable of downloading cockpit modules as jarfiles from an url.
 */
public class CockpitRemoteLibraryHandler implements LibraryHandler<Object>, LibraryFetcher
{
    private static final Logger LOG = LoggerFactory.getLogger(CockpitRemoteLibraryHandler.class);


    @Override
    public boolean canFetchLibrary(final ModuleInfo moduleInfo)
    {
        try
        {
            new URL(moduleInfo.getWidgetsPackage());
            return true;
        }
        catch(final MalformedURLException e)
        {
            return false;
        }
    }


    @Override
    public void fetchLibrary(final ModuleInfo moduleInfo, final File file) throws CockpitModuleDeploymentException
    {
        try
        {
            final URL url = new URL(moduleInfo.getWidgetsPackage());
            LOG.info("Downloading widget package from {}", url);
            FileUtils.copyURLToFile(url, file, 1000, 10000);
        }
        catch(final IOException e)
        {
            throw new CockpitModuleDeploymentException(e.getMessage(), e);
        }
    }


    @Override
    public void fetchLibrary(final CockpitModuleInfo moduleInfo, final File archiveFile) throws CockpitModuleDeploymentException
    {
        fetchLibrary((ModuleInfo)moduleInfo, archiveFile);
    }


    @Override
    public void afterDeploy(final CockpitModuleInfo moduleInfo, final String libdir)
    {
        // Do nothing
    }


    @Override
    public void afterDeployReverseOrder(final CockpitModuleInfo moduleInfo, final String libDir)
    {
        // Do nothing
    }


    @Override
    public Object prepare(final ModuleInfo moduleInfo)
    {
        return null;
    }


    @Override
    public void initialize(final ModuleInfo moduleInfo, final Object o)
    {
        // Do nothing
    }
}
