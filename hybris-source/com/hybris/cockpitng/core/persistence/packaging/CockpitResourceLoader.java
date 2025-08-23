/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

/**
 * Used by some classes to load resources other than by {@link ClassLoader#getResourceAsStream(String)}.
 */
public interface CockpitResourceLoader extends ResourceLoader
{
    /**
     * Returns a view file (e.g. .zul) for the given path as {@link InputStream}.
     */
    InputStream getViewResourceAsStream(String path);


    /**
     * Returns a view file (e.g. .zul) for the given path and {@link WidgetJarLibInfo} as a {@link String}
     */
    String getViewResourceAsString(WidgetJarLibInfo jarLibInfo, String widgetFilename);


    @Override
    default boolean hasResource(final String path)
    {
        try(InputStream stream = getResourceAsStream(path))
        {
            return stream != null;
        }
        catch(final IOException e)
        {
            LoggerFactory.getLogger(getClass()).error(e.getLocalizedMessage(), e);
            return false;
        }
    }


    /**
     * Returns a resource file for the given path and {@link WidgetJarLibInfo} as a {@link InputStream}
     */
    InputStream getResourceAsStream(WidgetJarLibInfo jarLibInfo, String widgetFilename);


    default InputStream getResourceAsStream(final File moduleJar, final String path)
    {
        final WidgetJarLibInfo info = new WidgetJarLibInfo(null, new Properties(), moduleJar, StringUtils.EMPTY);
        return getResourceAsStream(info, path);
    }


    /**
     * Checks whether a resource exists and may be read
     *
     * @param path path to resource
     * @param jarLibInfo widget, which resource is searched
     * @return <code>true</code> if resource is available
     */
    default boolean hasResource(final WidgetJarLibInfo jarLibInfo, final String path)
    {
        try(InputStream is = getResourceAsStream(jarLibInfo, path))
        {
            return is != null;
        }
        catch(final IOException e)
        {
            LoggerFactory.getLogger(getClass()).error(e.getLocalizedMessage(), e);
            return false;
        }
    }


    default boolean hasResource(final File moduleJar, final String path)
    {
        final WidgetJarLibInfo info = new WidgetJarLibInfo(null, new Properties(), moduleJar, StringUtils.EMPTY);
        return hasResource(info, path);
    }
}
