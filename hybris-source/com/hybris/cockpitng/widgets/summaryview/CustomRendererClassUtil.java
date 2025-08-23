/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview;

import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CustomRendererClassUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(CustomRendererClassUtil.class);


    private CustomRendererClassUtil()
    {
        //Utility class
    }


    /**
     * Returns an instance of {@link WidgetComponentRenderer} represented by Spring bean name or a class name or
     * <code>null</code> if it couldn't be found. If both <code>beanName</code> and <code>className</code> are specified and
     * not blank, <code>beanName</code> takes precedence.
     *
     * @param name
     *           Spring bean name to load from application context or class name to load from classloader
     * @return renderer instance
     */
    public static WidgetComponentRenderer createRenderer(final String name)
    {
        if(StringUtils.isNotBlank(name) && BackofficeSpringUtil.containsBean(name))
        {
            return createRenderer(name, StringUtils.EMPTY);
        }
        else
        {
            return createRenderer(StringUtils.EMPTY, name);
        }
    }


    /**
     * Returns an instance of {@link WidgetComponentRenderer} represented by Spring bean name or a class name or
     * <code>null</code> if it couldn't be found. If both <code>beanName</code> and <code>className</code> are specified and
     * not blank, <code>beanName</code> takes precedence.
     *
     * @param name
     *           Spring bean name to load from application context or class name to load from classloader
     * @param rendererClass
     *           expected renderer class
     * @return renderer instance
     */
    public static <R extends WidgetComponentRenderer> R createRenderer(final String name, final Class<? extends R> rendererClass)
    {
        if(StringUtils.isNotBlank(name) && BackofficeSpringUtil.containsBean(name))
        {
            return createRenderer(name, StringUtils.EMPTY, rendererClass);
        }
        else
        {
            return createRenderer(StringUtils.EMPTY, name, rendererClass);
        }
    }


    /**
     * Returns an instance of {@link WidgetComponentRenderer} represented by Spring bean name or a class name or
     * <code>null</code> if it couldn't be found. If both <code>beanName</code> and <code>className</code> are specified and
     * not blank, <code>beanName</code> takes precedence.
     *
     * @param beanName
     *           Spring bean name to load from application context
     * @param className
     *           class name to load from classloader
     * @return renderer instance
     */
    public static WidgetComponentRenderer createRenderer(final String beanName, final String className)
    {
        return createRenderer(beanName, className, WidgetComponentRenderer.class);
    }


    /**
     * Returns an instance of {@link WidgetComponentRenderer} represented by Spring bean name or a class name or
     * <code>null</code> if it couldn't be found. If both <code>beanName</code> and <code>className</code> are specified and
     * not blank, <code>beanName</code> takes precedence.
     *
     * @param beanName
     *           Spring bean name to load from application context
     * @param className
     *           class name to load from classloader
     * @param rendererClass
     *           expected renderer class
     * @return renderer instance
     */
    public static <RENDERER extends WidgetComponentRenderer> RENDERER createRenderer(final String beanName, final String className,
                    final Class<? extends RENDERER> rendererClass)
    {
        if(StringUtils.isNotEmpty(beanName))
        {
            return BackofficeSpringUtil.getBean(beanName);
        }
        else if(StringUtils.isNotEmpty(className))
        {
            return BackofficeSpringUtil.createClassInstance(className, rendererClass);
        }
        else
        {
            LOG.warn("Could not create renderer for bean name [{}] and/or class name [{}]", beanName, className);
            return null;
        }
    }
}
