/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

import com.hybris.cockpitng.core.util.Resettable;
import java.io.InputStream;

/**
 * A class loader aware of cockpit modules and capable of reading resources of particular module and/or component.
 */
public abstract class CockpitClassLoader extends ClassLoader implements Resettable
{
    public CockpitClassLoader(final ClassLoader parent)
    {
        super(parent);
    }


    public CockpitClassLoader()
    {
    }


    /**
     * Reads a resource related to specified component.
     *
     * @param componentInfo
     *           component information
     * @param resourceName
     *           relative path to resource
     * @return a stream to resource or <code>null</code> if resource cannot be found
     */
    public abstract InputStream getResourceAsStream(final WidgetJarLibInfo componentInfo, final String resourceName);
}
