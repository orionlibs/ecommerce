/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging.impl;

import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.util.zip.SafeZipEntry;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.springframework.beans.factory.annotation.Required;

/**
 * Widget resource reader that looks-up in widget's jar files for searched resources
 */
public class WidgetJarResourceReader extends AbstractCockpitResourceReader
{
    private static final String ERROR_MESSAGE_PATTERN_LOADING = "Error loading resource '%s' from jar '%s'";
    private WidgetLibUtils widgetLibUtils;


    @Override
    public InputStream getResourceAsStream(final String path)
    {
        return null;
    }


    @Override
    public boolean hasResource(final String path)
    {
        return false;
    }


    @Override
    public InputStream getResourceAsStream(final WidgetJarLibInfo widgetJarLibInfo, final String resourceName)
    {
        if(widgetJarLibInfo != null)
        {
            try
            {
                final JarFile jarFile = new JarFile(widgetJarLibInfo.getJarPath());
                return getResourceAsStream(jarFile, getFullResourceName(widgetJarLibInfo, resourceName));
            }
            catch(final IOException e)
            {
                LOG.error(String.format(ERROR_MESSAGE_PATTERN_LOADING, resourceName, widgetJarLibInfo.getJarName()), e);
            }
        }
        return null;
    }


    @Override
    public boolean hasResource(final WidgetJarLibInfo jarLibInfo, final String path)
    {
        if(jarLibInfo != null)
        {
            final String resourceName = getFullResourceName(jarLibInfo, path);
            try(final JarFile jarfile = new JarFile(jarLibInfo.getJarPath()))
            {
                return jarfile.getJarEntry(resourceName.trim()) != null;
            }
            catch(final IOException e)
            {
                LOG.error(e.getLocalizedMessage(), e);
            }
        }
        return false;
    }


    @Override
    public InputStream getResourceAsStream(final File moduleJar, final String path)
    {
        if(moduleJar.exists() && moduleJar.canRead())
        {
            try
            {
                final JarFile jarfile = new JarFile(moduleJar);
                return getResourceAsStream(jarfile, path);
            }
            catch(final IOException e)
            {
                LOG.error(String.format(ERROR_MESSAGE_PATTERN_LOADING, path, moduleJar.getName()), e);
            }
        }
        return null;
    }


    protected InputStream getResourceAsStream(final JarFile jarfile, final String path)
    {
        final String resourceName = getFullResourceName((String)null, path);
        try
        {
            final Enumeration<JarEntry> entries = jarfile.entries();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Try to find resource for '{}' in '{}'.", resourceName, jarfile.getName());
            }
            while(entries.hasMoreElements())
            {
                final ZipEntry entry = entries.nextElement();
                if(entry.getName().equals(resourceName.trim()))
                {
                    return jarfile.getInputStream(new SafeZipEntry(entry));
                }
            }
        }
        catch(final IOException e)
        {
            LOG.debug("Try to find resource for '" + resourceName + "' in '" + jarfile.getName() + "'.", e);
        }
        return null;
    }


    @Override
    public boolean hasResource(final File moduleJar, final String path)
    {
        final String resourceName = getFullResourceName((String)null, path);
        try(final JarFile jarfile = new JarFile(moduleJar))
        {
            return jarfile.getJarEntry(resourceName.trim()) != null;
        }
        catch(final IOException ex)
        {
            LOG.error(ex.getLocalizedMessage(), ex);
            return false;
        }
    }


    protected WidgetLibUtils getWidgetLibUtils()
    {
        return widgetLibUtils;
    }


    @Required
    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }
}
