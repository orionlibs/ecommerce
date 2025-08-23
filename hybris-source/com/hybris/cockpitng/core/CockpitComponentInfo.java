/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import java.util.Date;
import java.util.Properties;

/**
 * Raw cockpit component info object as provided by cockpit component loader.
 */
public class CockpitComponentInfo
{
    private Properties properties;
    private String rootPath;
    private String externalLocation;
    private final Date creationDate;
    private ClassLoader classLoader;


    public CockpitComponentInfo(final Properties properties, final String rootPath, final ClassLoader classLoader)
    {
        this.properties = properties;
        this.rootPath = rootPath;
        this.classLoader = classLoader;
        creationDate = new Date();
    }


    public Properties getProperties()
    {
        return properties;
    }


    public void setProperties(final Properties properties)
    {
        this.properties = properties;
    }


    public String getRootPath()
    {
        return rootPath;
    }


    public void setRootPath(final String rootPath)
    {
        this.rootPath = rootPath;
    }


    public ClassLoader getClassLoader()
    {
        return classLoader;
    }


    public void setClassLoader(final ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }


    public String getExternalLocation()
    {
        return externalLocation;
    }


    public void setExternalLocation(final String externalLocation)
    {
        this.externalLocation = externalLocation;
    }


    public Date getCreationDate()
    {
        return (Date)creationDate.clone();
    }
}
