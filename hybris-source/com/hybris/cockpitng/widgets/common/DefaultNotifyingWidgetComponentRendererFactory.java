/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultNotifyingWidgetComponentRendererFactory extends DefaultWidgetComponentRendererFactory
                implements NotifyingWidgetComponentRendererFactory
{
    private final static Logger LOG = LoggerFactory.getLogger(DefaultNotifyingWidgetComponentRendererFactory.class);


    @Override
    public NotifyingWidgetComponentRenderer createWidgetComponentRenderer(final String configuredRenderer)
    {
        final WidgetComponentRenderer renderer = super.createWidgetComponentRenderer(configuredRenderer);
        if(renderer instanceof NotifyingWidgetComponentRenderer)
        {
            return (NotifyingWidgetComponentRenderer)renderer;
        }
        else
        {
            LOG.warn("Could not load renderer bean with id {}.", configuredRenderer);
            return null;
        }
    }
}
