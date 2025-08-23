/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.impl;

import java.io.File;
import java.net.URI;

/**
 *
 *
 */
public class WidgetRequest
{
    private final String root;
    private final File jarFile;
    private final URI resource;
    private String widgetId;


    public WidgetRequest(final String root, final File jarFile, final String resource)
    {
        this.root = root;
        this.jarFile = jarFile;
        this.resource = URI.create(resource);
    }


    public String getRoot()
    {
        return root;
    }


    public String getWidgetId()
    {
        return widgetId;
    }


    public void setWidgetId(final String widgetId)
    {
        this.widgetId = widgetId;
    }


    public String getResourcePath()
    {
        return resource.getPath();
    }


    public URI getResource()
    {
        return resource;
    }


    public File getJarFile()
    {
        return jarFile;
    }
}
