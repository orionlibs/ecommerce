/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util;

import java.io.InputStream;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utils associated with "current" class loader. Generally as the current class loader, the thread current classloader
 * is taken. If it does not exist then the class loader of an available class is taken.
 */
public final class ClassLoaderUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoaderUtils.class);


    private ClassLoaderUtils()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    /**
     * Returns the "current" class loader. Generally as the current class loader, the thread current classloader is taken.
     * If it does not exist then the class loader of the provided class is taken.
     *
     * @param clazz
     *           class to get class loader of in case the thread current class loader is not available
     * @return current class loader
     */
    public static ClassLoader getCurrentClassLoader(final Class<?> clazz)
    {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader != null)
        {
            try
            {
                Class.forName(clazz.getName(), false, classLoader);
                return classLoader;
            }
            catch(final ClassNotFoundException e)
            {
                LOGGER.debug(e.getLocalizedMessage(), e);
            }
        }
        return clazz.getClassLoader();
    }


    /**
     * Returns an input stream for reading the specified resource.
     * <p>
     * This method will search classloader from {@link #getCurrentClassLoader(Class)} for the resource;
     * </p>
     *
     * @param clazz
     *           Relative class, which classloader will be used to localize resource
     * @param resource
     *           The resource name
     * @return An input stream for reading the resource, or <p>null</p> if the resource could not be found
     */
    public static InputStream getResourceAsStream(final Class<?> clazz, final String resource)
    {
        final InputStream classResource = clazz.getResourceAsStream(resource);
        if(classResource == null && resource.startsWith(String.valueOf(IOUtils.DIR_SEPARATOR_UNIX)))
        {
            final String adoptedResource = StringUtils.removeStart(resource, String.valueOf(IOUtils.DIR_SEPARATOR_UNIX));
            return getCurrentClassLoader(clazz).getResourceAsStream(adoptedResource);
        }
        else if(classResource == null)
        {
            final String adoptedResource = clazz.getPackage().getName().replaceAll("\\.", String.valueOf(IOUtils.DIR_SEPARATOR_UNIX))
                            + String.valueOf(IOUtils.DIR_SEPARATOR_UNIX) + resource;
            return getCurrentClassLoader(clazz).getResourceAsStream(adoptedResource);
        }
        else
        {
            return classResource;
        }
    }


    /**
     * Finds the resource with the given name. A resource is some data (images, audio, text, etc) that can be accessed by
     * class code in a way that is independent of the location of the code.
     * <p>
     * The name of a resource is a '<p>/</p>'-separated path name that identifies the resource.
     * <p>
     * This method will search classloader from {@link #getCurrentClassLoader(Class)} for the resource
     * </p>
     *
     * @param clazz
     *           Relative class, which classloader will be used to localize resource
     * @param resource
     *           The resource name
     * @return A <p>URL</p> object for reading the resource, or <p>null</p> if the resource could not be found or the
     *         invoker doesn't have adequate privileges to get the resource.
     */
    public static URL getResource(final Class<?> clazz, final String resource)
    {
        final URL classResource = clazz.getResource(resource);
        if(classResource == null && resource.startsWith(String.valueOf(IOUtils.DIR_SEPARATOR_UNIX)))
        {
            final String adoptedResource = StringUtils.removeStart(resource, String.valueOf(IOUtils.DIR_SEPARATOR_UNIX));
            return getCurrentClassLoader(clazz).getResource(adoptedResource);
        }
        else if(classResource == null)
        {
            final String adoptedResource = clazz.getPackage().getName().replaceAll("\\.", String.valueOf(IOUtils.DIR_SEPARATOR_UNIX))
                            + String.valueOf(IOUtils.DIR_SEPARATOR_UNIX) + resource;
            return getCurrentClassLoader(clazz).getResource(adoptedResource);
        }
        else
        {
            return classResource;
        }
    }
}
