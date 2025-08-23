/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging.impl;

import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for cockpit resource readers.
 */
public abstract class AbstractCockpitResourceReader implements CockpitResourceLoader
{
    protected final Logger LOG = LoggerFactory.getLogger(getClass());


    protected static String adaptResourceName(final String name)
    {
        return name.charAt(0) == '/' ? name.substring(1) : name;
    }


    protected static String getFullResourceName(final WidgetJarLibInfo jarLibInfo, final String widgetFilename)
    {
        return getFullResourceName(jarLibInfo == null ? StringUtils.EMPTY : jarLibInfo.getPrefix(), widgetFilename);
    }


    protected static String getFullResourceName(final String prefix, final String widgetFilename)
    {
        final boolean addPrefix = !widgetFilename.startsWith("/");
        final String widgetFilenameAdapted = adaptResourceName(widgetFilename);
        if(addPrefix && StringUtils.isNotBlank(prefix))
        {
            return prefix + "/" + widgetFilenameAdapted;
        }
        return widgetFilenameAdapted;
    }


    @Override
    public InputStream getViewResourceAsStream(final String path)
    {
        return getResourceAsStream(path);
    }


    @Override
    public String getViewResourceAsString(final WidgetJarLibInfo jarLibInfo, final String widgetFilename)
    {
        try(final InputStream resourceAsStream = getResourceAsStream(jarLibInfo, widgetFilename))
        {
            if(resourceAsStream != null)
            {
                return IOUtils.toString(resourceAsStream);
            }
        }
        catch(final IOException e)
        {
            LOG.error("Error reading " + widgetFilename, e);
        }
        return null;
    }
}
