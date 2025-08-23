/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dependencies.impl;

import com.hybris.cockpitng.dependencies.factory.impl.DependencyResolver;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;

/**
 * Resolves dependencies based on Spring Application Context and injects them to non-spring beans.
 *
 * @param <T>
 */
public class SpringApplicationContextDependencyResolver<T> implements DependencyResolver<T>
{
    private final ApplicationContext context;
    private final static Map<ApplicationContext, ApplicationContext> cachedAnnotationEnabledAppCtx = new ConcurrentHashMap<>();


    /**
     * @param context
     *           - spring application context to search for dependencies in it
     */
    public SpringApplicationContextDependencyResolver(final ApplicationContext context)
    {
        if(cachedAnnotationEnabledAppCtx.containsKey(context))
        {
            this.context = cachedAnnotationEnabledAppCtx.get(context);
        }
        else if(context.getBeansOfType(CommonAnnotationBeanPostProcessor.class).isEmpty())
        {
            final AnnotationConfigApplicationContext temp = new AnnotationConfigApplicationContext(
                            ApplicationConfigWithEnabledAnnotationConfig.class);
            temp.setParent(context);
            this.context = temp;
            cachedAnnotationEnabledAppCtx.put(context, temp);
        }
        else
        {
            this.context = context;
            cachedAnnotationEnabledAppCtx.put(context, context);
        }
    }


    /**
     * Injects spring dependencies to object which does not have to be spring-managed bean. Dependencies are provided for
     * well known annotations like: Resource, Autowired.
     *
     * @param obj
     *           - object to inject dependencies
     */
    public void injectDependencies(final T obj)
    {
        context.getAutowireCapableBeanFactory().autowireBeanProperties(obj, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
    }
}
