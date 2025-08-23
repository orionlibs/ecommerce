/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

import java.io.File;
import java.util.Properties;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains information about single component defined in a module library.
 */
public class WidgetJarLibInfo
{
    private static final Logger LOG = LoggerFactory.getLogger(WidgetJarLibInfo.class);
    private final String id;
    private final Properties properties;
    private final File jarFile;
    private final String prefix;
    private String externalLocation;


    /**
     * @param id
     *           id of component
     * @param properties
     *           properties of component
     * @param jarFile
     *           library file of defining module
     * @param prefix
     *           path prefix for components resources
     */
    public WidgetJarLibInfo(final String id, final Properties properties, final File jarFile, final String prefix)
    {
        this.id = id;
        this.properties = properties;
        this.jarFile = jarFile;
        this.prefix = prefix;
    }


    /**
     * Retrieves component's definition identity
     *
     * @return id of component
     */
    public String getId()
    {
        return id;
    }


    public Properties getProperties()
    {
        return properties;
    }


    /**
     * Retrieves a path prefix for components resources
     *
     * @return resources prefix
     */
    public String getPrefix()
    {
        return prefix;
    }


    /**
     * @deprecated since 1808, use {@link #getJarPath()} instead
     */
    @Deprecated(since = "1808", forRemoval = true)
    public JarFile getJarFile()
    {
        throw new UnsupportedOperationException("Opening jar file requires it's closer. Please open module library with caution.");
    }


    /**
     * Retrieves a name of defining module's library
     *
     * @return name of module library
     */
    public String getJarName()
    {
        return jarFile.getName();
    }


    /**
     * Retrieves a defining module's library
     *
     * @return module library
     */
    public File getJarPath()
    {
        return jarFile;
    }


    /**
     * Retrieves a URI to defining module source
     *
     * @return module source URI
     */
    public String getExternalLocation()
    {
        return externalLocation;
    }


    /**
     * @param externalLocation
     */
    public void setExternalLocation(final String externalLocation)
    {
        this.externalLocation = externalLocation;
    }
}
