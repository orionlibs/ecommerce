/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.zkoss.spring.SpringUtil;

/**
 * Utility methods for access the cockpit module application context.
 */
public final class BackofficeSpringUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeSpringUtil.class);


    private BackofficeSpringUtil()
    {
        // utility class
    }


    /**
     * Checks if bean for the given name exists. First it checks, if the bean is in the cockpit module application context.
     * If not, it falls back to {@link SpringUtil#getBean(String)}.
     */
    public static boolean containsBean(final String name)
    {
        boolean ret = false;
        final ApplicationContext applicationContext = getApplicationContext();
        if(applicationContext != null)
        {
            ret = applicationContext.containsBean(name);
        }
        return ret || SpringUtil.getBean(name) != null;
    }


    /**
     * Gets the bean for the given name. First it checks, if the bean is in the cockpit module application context. If not,
     * it falls back to {@link SpringUtil#getBean(String)}.
     */
    public static <C> C getBean(final String name)
    {
        Object ret = null;
        final ApplicationContext applicationContext = getApplicationContext();
        if(applicationContext != null)
        {
            try
            {
                if(applicationContext.containsBean(name))
                {
                    ret = applicationContext.getBean(name);
                }
            }
            catch(final BeansException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(
                                    String.format(
                                                    "Bean '{}' not found in cockpit module application context, trying to find in parent contexts.", name),
                                    e);
                }
            }
        }
        return (C)(ret == null ? SpringUtil.getBean(name) : ret);
    }


    /**
     * Allows to get a bean and provide constructor args in runtime. Common use case is when bean with prototype scope is
     * needed with some specific runtime data passed into constructor.
     *
     * @param name
     *           bean name
     * @param args
     *           constructor args which can be passed to bean
     */
    public static <C> C getBean(final String name, final Object... args)
    {
        Object ret = null;
        final ApplicationContext applicationContext = getApplicationContext();
        if(applicationContext != null)
        {
            try
            {
                if(applicationContext.containsBean(name))
                {
                    ret = applicationContext.getBean(name, args);
                }
            }
            catch(final BeansException e)
            {
                LOG.debug(
                                "Bean '" + name + "' not found in cockpit module application context, " + "trying to find in parent contexts.",
                                e);
            }
        }
        return (C)(ret == null ? SpringUtil.getApplicationContext().getBean(name, args) : ret);
    }


    /**
     * Gets the bean for the given name and class. First it checks, if the bean is in the cockpit module application
     * context. If not, it falls back to {@link SpringUtil#getBean(String, Class)}.
     */
    public static <C> C getBean(final String name, final Class<? extends C> clz)
    {
        Object ret = null;
        final ApplicationContext applicationContext = getApplicationContext();
        if(applicationContext != null)
        {
            try
            {
                if(applicationContext.containsBean(name))
                {
                    ret = getApplicationContext().getBean(name, clz);
                }
            }
            catch(final BeanNotOfRequiredTypeException e)
            {
                LOG.debug(String.format(
                                "Bean '%s' not found in cockpit module application context, trying to find in parent contexts.", name), e);
            }
        }
        return clz.cast(ret == null ? SpringUtil.getBean(name, clz) : ret);
    }


    /**
     * Gets all beans for given class. Starts in cockpit module application context and goes all the way up to global
     * context.
     */
    public static <T> Map<String, T> getAllBeans(final Class<T> clazz)
    {
        return getAllBeans(clazz, false);
    }


    /**
     * Gets all beans for given class can respect the autowire-candidate flag for bean. Starts in cockpit module application
     * context and goes all the way up to global context.
     *
     * @param clazz
     *           - given class
     * @param respectAutowireFlag
     *           - whether autowire-candidate attribute should be respected
     */
    public static <T> Map<String, T> getAllBeans(final Class<T> clazz, final boolean respectAutowireFlag)
    {
        final Map<String, T> beans = Maps.newHashMap();
        ApplicationContext context = getApplicationContext();
        while(context != null)
        {
            for(final Map.Entry<String, T> entry : context.getBeansOfType(clazz).entrySet())
            {
                if(!beans.containsKey(entry.getKey()))
                {
                    final boolean shouldBeAdded = respectAutowireFlag && isAutowireCandidate(context, entry.getKey())
                                    || !respectAutowireFlag;
                    if(shouldBeAdded)
                    {
                        beans.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            context = context.getParent();
        }
        return beans;
    }


    protected static boolean isAutowireCandidate(final ApplicationContext applicationContext, final String beanName)
    {
        boolean autowireCandidate = true;
        if(applicationContext != null
                        && applicationContext.getAutowireCapableBeanFactory() instanceof ConfigurableListableBeanFactory)
        {
            final BeanDefinition beanDefinition = ((ConfigurableListableBeanFactory)applicationContext
                            .getAutowireCapableBeanFactory()).getBeanDefinition(beanName);
            if(beanDefinition != null)
            {
                autowireCandidate = beanDefinition.isAutowireCandidate();
            }
        }
        return autowireCandidate;
    }


    /**
     * Gets the cockpit module application context, if existing, otherwise null.
     */
    public static CockpitApplicationContext getApplicationContext()
    {
        return CockpitApplicationContext.getCockpitApplicationContext(SpringUtil.getApplicationContext());
    }


    /**
     * Loads a class by class name using cockpit module class loader and returns a new instance of the class.
     *
     * @param className
     *           class name to create.
     * @param clazz
     *           class used for generalization
     * @param <T>
     *           type of returned instance
     * @return new instance off clazz
     */
    public static <T> T createClassInstance(final String className, final Class<T> clazz)
    {
        try
        {
            return Class.forName(className, true, getApplicationContext().getClassLoader()).asSubclass(clazz).newInstance();
        }
        catch(final ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            if(LOG.isErrorEnabled())
            {
                LOG.error("Class could not be instantiated.", e);
            }
            return null;
        }
    }


    /**
     * Loads a class by class name using cockpit module class loader and returns it.
     *
     * @param className
     *           class name to create.
     * @return class loaded by class name using cockpit module class loader
     */
    public static <T> T loadClass(final String className)
    {
        try
        {
            return (T)Class.forName(className, true, getApplicationContext().getClassLoader());
        }
        catch(final ClassNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Class could not be found.", e);
            }
            return null;
        }
    }


    /**
     * A method designed to be used in situations when a bean is stored in a class field and should be searched only once:
     * <br><br>
     * <code>
     *             someField = BackofficeSpringUtil.getBeanForField("someBean", someField);<br>
     *             return someField;
     * </code>
     *
     * @param beanName name of a bean to be found
     * @param fieldValue current value of a field
     * @param <T> expected type of bean
     * @return a bean instance
     */
    public static <T> T getBeanForField(final String beanName, final T fieldValue)
    {
        return fieldValue != null ? fieldValue : BackofficeSpringUtil.getBean(beanName);
    }
}
