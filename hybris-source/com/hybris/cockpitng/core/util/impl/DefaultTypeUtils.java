/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.impl;

import com.hybris.cockpitng.core.util.ClassLoaderUtils;
import com.hybris.cockpitng.core.util.CockpitTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTypeUtils implements CockpitTypeUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTypeUtils.class);


    @Override
    public boolean isAssignableFrom(final String superType, final String subType)
    {
        final ClassLoader loader = ClassLoaderUtils.getCurrentClassLoader(this.getClass());
        if(loader != null)
        {
            final Class<?> inputClass = loadClass(superType, loader);
            final Class<?> outputClass = loadClass(subType, loader);
            if(inputClass != null && outputClass != null)
            {
                return inputClass.isAssignableFrom(outputClass);
            }
        }
        else
        {
            LOG.warn("Could not resolve class loader");
        }
        return false;
    }


    protected Class loadClass(final String type, final ClassLoader loader)
    {
        try
        {
            return loader.loadClass(type);
        }
        catch(final ClassNotFoundException exception)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not resolve type: " + type, exception);
            }
            else
            {
                LOG.warn("Could not resolve type: " + type);
            }
        }
        return null;
    }
}
