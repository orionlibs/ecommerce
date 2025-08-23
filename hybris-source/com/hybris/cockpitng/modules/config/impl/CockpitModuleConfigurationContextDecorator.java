/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.WidgetConfigurationContextDecorator;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import org.apache.commons.lang3.StringUtils;

/**
 * Adds the "_root_" as the module context if no module context is set.
 */
public class CockpitModuleConfigurationContextDecorator implements WidgetConfigurationContextDecorator
{
    /**
     * name of the module context
     */
    public static final String MODULE_CONTEXT_NAME = "module";
    /**
     * value of the root module context
     */
    public static final String ROOT_MODULE_CONTEXT_VALUE = "_root_";


    @Override
    public <CONFIG> ConfigContext decorateContext(final ConfigContext context, final Class<CONFIG> configClass,
                    final WidgetInstance widgetInstance)
    {
        final DefaultConfigContext decorated = new DefaultConfigContext();
        for(final String name : context.getAttributeNames())
        {
            decorated.addAttribute(name, context.getAttribute(name));
        }
        final String module = context.getAttribute(MODULE_CONTEXT_NAME);
        if(StringUtils.isBlank(module))
        {
            decorated.addAttribute(MODULE_CONTEXT_NAME, ROOT_MODULE_CONTEXT_VALUE);
        }
        return decorated;
    }
}
