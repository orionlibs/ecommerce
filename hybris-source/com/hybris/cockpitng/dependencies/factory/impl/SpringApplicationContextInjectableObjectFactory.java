/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dependencies.factory.impl;

import com.hybris.cockpitng.dependencies.impl.SpringApplicationContextDependencyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Factory enables create an object of a certain type T and inject spring dependencies to the created object
 *
 * @param <T>
 */
public class SpringApplicationContextInjectableObjectFactory<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(SpringApplicationContextInjectableObjectFactory.class);
    private final DependencyResolver<T> dependencyResolver;
    private final Class<T> clazz;


    /**
     * @param context               - spring application context to search for dependencies in it
     * @param clazz                 - type of new object
     */
    public SpringApplicationContextInjectableObjectFactory(
                    final ApplicationContext context, final Class<T> clazz)
    {
        dependencyResolver = new SpringApplicationContextDependencyResolver<>(
                        context);
        this.clazz = clazz;
    }


    /**
     * Creates new object
     *
     * @return
     */
    protected T create()
    {
        T obj;
        try
        {
            obj = clazz.newInstance();
        }
        catch(final Exception ex)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error while creating new object", ex);
            }
            obj = null;
        }
        return obj;
    }


    /**
     * Injects dependencies to obj
     *
     * @param obj
     */
    protected void injectDependencies(final T obj)
    {
        dependencyResolver.injectDependencies(obj);
    }


    /**
     * Creates new object and then injects dependencies
     *
     * @return
     */
    public T createAndInjectDependencies()
    {
        final T obj = create();
        injectDependencies(obj);
        return obj;
    }
}
