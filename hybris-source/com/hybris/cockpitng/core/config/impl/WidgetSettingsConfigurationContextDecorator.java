/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.WidgetConfigurationContextDecorator;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import org.apache.commons.lang3.StringUtils;

/**
 * Provides additional configuration context from the widget settings.
 */
public class WidgetSettingsConfigurationContextDecorator implements WidgetConfigurationContextDecorator
{
    private static final String CONFIG_CONTEXT = "config.context.";


    @Override
    public <C> ConfigContext decorateContext(final ConfigContext context, final Class<C> configurationType,
                    final WidgetInstance widgetInstance)
    {
        if(widgetInstance != null && widgetInstance.getWidget() != null)
        {
            final DefaultConfigContext decorated = new DefaultConfigContext();
            for(final String name : context.getAttributeNames())
            {
                decorated.addAttribute(name, context.getAttribute(name));
            }
            final TypedSettingsMap settings = widgetInstance.getWidget().getWidgetSettings();
            for(final String key : settings.keySet())
            {
                if(!StringUtils.startsWith(key, CONFIG_CONTEXT))
                {
                    continue;
                }
                final String value = settings.getString(key);
                if(!StringUtils.isBlank(value))
                {
                    decorated.addAttribute(key.substring(CONFIG_CONTEXT.length()).trim(), value.trim());
                }
            }
            return decorated;
        }
        else
        {
            return context;
        }
    }
}
