/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging.impl;

import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.core.util.ClassLoaderUtils;
import java.io.File;
import java.io.InputStream;

/**
 * Widget resource reader that looks-up in module's classpath for searched resources
 *
 */
public class WidgetClasspathResourceReader extends AbstractCockpitResourceReader
{
    @Override
    public InputStream getResourceAsStream(final String path)
    {
        return ClassLoaderUtils.getCurrentClassLoader(this.getClass()).getResourceAsStream(adaptResourceName(path));
    }


    @Override
    public boolean hasResource(final String path)
    {
        return ClassLoaderUtils.getCurrentClassLoader(this.getClass()).getResource(adaptResourceName(path)) != null;
    }


    @Override
    public InputStream getResourceAsStream(final WidgetJarLibInfo widgetJarLibInfo, final String resourceName)
    {
        return getResourceAsStream(resourceName);
    }


    @Override
    public boolean hasResource(final WidgetJarLibInfo jarLibInfo, final String path)
    {
        return false;
    }


    @Override
    public InputStream getResourceAsStream(final File moduleJar, final String path)
    {
        return null;
    }


    @Override
    public boolean hasResource(final File moduleJar, final String path)
    {
        return false;
    }
}
