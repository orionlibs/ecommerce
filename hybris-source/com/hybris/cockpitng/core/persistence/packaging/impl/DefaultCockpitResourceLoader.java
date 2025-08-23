/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging.impl;

import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.core.util.CockpitProperties;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.Ordered;

/**
 * Implementation of {@link org.springframework.core.io.ResourceLoader} that allows scanning files specified by a list
 * of root directories.
 */
public class DefaultCockpitResourceLoader extends AbstractCockpitResourceReader implements Ordered
{
    public static final int ORDER = 1000;
    public static final String ENABLED_KEY = "cockpitng.additionalResourceLoader.enabled";
    private static final String PATH_KEY = "cockpitng.additionalResourceLoader.path";
    private CockpitProperties cockpitProperties;
    private Boolean enabledCached;
    private final Map<String, File> fileCache = new HashMap<>();
    private volatile List<String> paths;


    /**
     * Initializes the list of path entries according to the cockpit property value if necessary and returns it.
     */
    protected List<String> getPathEntries()
    {
        if(paths == null)
        {
            synchronized(this)
            {
                paths = new ArrayList<>();
                final String property = getCockpitProperties().getProperty(PATH_KEY);
                if(property != null && property.contains(";"))
                {
                    final String[] split = property.split(";");
                    Collections.addAll(paths, split);
                }
            }
        }
        return Collections.unmodifiableList(paths);
    }


    @Override
    public InputStream getResourceAsStream(final String path)
    {
        if(isEnabled())
        {
            File file = fileCache.get(path);
            if(file == null)
            {
                file = resolveFile(path);
            }
            if(file != null)
            {
                fileCache.put(path, file);
                try
                {
                    return FileUtils.openInputStream(file);
                }
                catch(final IOException e)
                {
                    LOG.error("Could not open file " + file, e);
                }
            }
        }
        return null;
    }


    @Override
    public boolean hasResource(final String path)
    {
        if(isEnabled())
        {
            File file = fileCache.get(path);
            if(file == null)
            {
                file = resolveFile(path);
            }
            if(file != null)
            {
                fileCache.put(path, file);
            }
            return file != null;
        }
        return false;
    }


    /**
     * Tries to locate the file in one of the specified path entries.
     *
     * @return The file for the given path or null, if not existing.
     */
    protected File resolveFile(final String path)
    {
        for(final String pathEntry : getPathEntries())
        {
            final File file = new File(FilenameUtils.normalize(pathEntry + "/" + path));
            if(file.exists())
            {
                LOG.debug("Found resource: {}", file);
                return file;
            }
        }
        return null;
    }


    @Override
    public InputStream getResourceAsStream(final WidgetJarLibInfo jarLibInfo, final String filename)
    {
        String fullFilename = filename;
        if(jarLibInfo != null)
        {
            if(StringUtils.isNotBlank(jarLibInfo.getExternalLocation()))
            {
                return null;
            }
            final String prefix = jarLibInfo.getPrefix();
            fullFilename = filename.startsWith("/") ? "/" + prefix + "/" + filename : filename;
        }
        final InputStream resourceAsStream = getResourceAsStream(fullFilename);
        if(resourceAsStream != null)
        {
            return resourceAsStream;
        }
        return null;
    }


    @Override
    public boolean hasResource(final WidgetJarLibInfo jarLibInfo, final String path)
    {
        String fullFilename = path;
        if(jarLibInfo != null)
        {
            if(StringUtils.isNotBlank(jarLibInfo.getExternalLocation()))
            {
                return false;
            }
            final String prefix = jarLibInfo.getPrefix();
            fullFilename = path.startsWith("/") ? "/" + prefix + "/" + path : path;
        }
        return hasResource(fullFilename);
    }


    /**
     * Whether or not to use the whole mechanism, specified by {@link #ENABLED_KEY} cockpit property.
     *
     * @return If false is returned, {@link #getResourceAsStream(String)} will always return null.
     */
    protected boolean isEnabled()
    {
        if(enabledCached == null)
        {
            enabledCached = Boolean
                            .valueOf(getCockpitProperties() != null && Boolean.parseBoolean(getCockpitProperties().getProperty(ENABLED_KEY)));
        }
        return Boolean.TRUE.equals(enabledCached);
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Override
    public int getOrder()
    {
        return ORDER;
    }
}
