/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

import com.hybris.cockpitng.util.BackofficeSpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWidgetComponentRendererFactory implements WidgetComponentRendererFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetComponentRendererFactory.class);


    @Override
    public WidgetComponentRenderer createWidgetComponentRenderer(final String configuredRenderer)
    {
        WidgetComponentRenderer renderer = null;
        if(StringUtils.isNotBlank(configuredRenderer))
        {
            if(StringUtils.contains(configuredRenderer, '.'))
            {
                try
                {
                    renderer = BackofficeSpringUtil.createClassInstance(configuredRenderer, WidgetComponentRenderer.class);
                }
                catch(final Exception e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.warn(String.format("Configured renderer (%s) could not be loaded with message %s.", configuredRenderer, e.getMessage()), e);
                    }
                    else
                    {
                        LOG.warn("Configured renderer ({}) could not be loaded with message {}.", configuredRenderer, e.getMessage());
                    }
                }
            }
            else
            {
                try
                {
                    final Object bean = BackofficeSpringUtil.getBean(configuredRenderer, WidgetComponentRenderer.class);
                    if(bean instanceof WidgetComponentRenderer)
                    {
                        renderer = (WidgetComponentRenderer)bean;
                    }
                    else
                    {
                        LOG.warn("Could not load renderer bean with id {}.", configuredRenderer);
                    }
                }
                catch(final Exception e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.warn(String.format("Could not load renderer bean with id %s and message %s", configuredRenderer, e.getMessage()), e);
                    }
                    else
                    {
                        LOG.warn("Could not load renderer bean with id {} and message {}", configuredRenderer, e.getMessage());
                    }
                }
            }
        }
        return renderer;
    }
}
