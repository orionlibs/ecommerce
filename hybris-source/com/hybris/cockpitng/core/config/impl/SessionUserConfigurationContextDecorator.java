/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import static com.hybris.cockpitng.core.config.impl.DefaultConfigContext.CONTEXT_PRINCIPAL;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.WidgetConfigurationContextDecorator;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.user.CockpitUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Adds the user id retrieved from {@link CockpitUserService#getCurrentUser()} to the configuration context for key
 * {@link DefaultConfigContext#CONTEXT_PRINCIPAL}, if it doesn't exist already.
 */
public class SessionUserConfigurationContextDecorator implements WidgetConfigurationContextDecorator
{
    private CockpitUserService cockpitUserService;


    protected String getAttributeName()
    {
        return CONTEXT_PRINCIPAL;
    }


    protected boolean requiresDecoration(final ConfigContext context, final Class<?> configurationType,
                    final WidgetInstance widgetInstance, final String currentUser)
    {
        return StringUtils.isNotBlank(currentUser) && StringUtils.isBlank(context.getAttribute(getAttributeName()));
    }


    protected void decorate(final DefaultConfigContext context, final Class<?> configurationType,
                    final WidgetInstance widgetInstance, final String currentUser)
    {
        context.addAttribute(getAttributeName(), currentUser);
    }


    @Override
    public <C> ConfigContext decorateContext(final ConfigContext context, final Class<C> configurationType,
                    final WidgetInstance widgetInstance)
    {
        if(getCockpitUserService() == null)
        {
            return context;
        }
        final String currentUser = getCockpitUserService().getCurrentUser();
        if(!requiresDecoration(context, configurationType, widgetInstance, currentUser))
        {
            return context;
        }
        else
        {
            final DefaultConfigContext decorated = new DefaultConfigContext();
            for(final String name : context.getAttributeNames())
            {
                decorated.addAttribute(name, context.getAttribute(name));
            }
            decorate(decorated, configurationType, widgetInstance, currentUser);
            return decorated;
        }
    }


    protected CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    @Autowired(required = false)
    @Qualifier("cockpitUserService")
    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }
}
