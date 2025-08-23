/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import static com.hybris.cockpitng.core.config.impl.DefaultConfigContext.CONTEXT_AUTHORITY;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.util.CockpitProperties;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

/**
 * Adds the user id retrieved from {@link CockpitUserService#getCurrentUser()} to the configuration context for key
 * {@link DefaultConfigContext#CONTEXT_PRINCIPAL}, if it doesn't exist already.
 */
public class SessionAuthorityConfigurationContextDecorator extends SessionUserConfigurationContextDecorator
{
    private static final String PROPERTY_ENABLED = "cockpitng.configuration.authority.enabled";
    private static final String SETTING_ENABLED = "configuration.authority.enabled";
    private CockpitProperties cockpitProperties;


    @Override
    protected String getAttributeName()
    {
        return CONTEXT_AUTHORITY;
    }


    @Override
    public <C> ConfigContext decorateContext(final ConfigContext context, final Class<C> configurationType,
                    final WidgetInstance widgetInstance)
    {
        final Optional<Boolean> authorityWidgetSettingEnabled;
        if(widgetInstance == null || !widgetInstance.getWidget().getWidgetSettings().containsKey(SETTING_ENABLED))
        {
            authorityWidgetSettingEnabled = Optional.empty();
        }
        else
        {
            authorityWidgetSettingEnabled = Optional.of(widgetInstance.getWidget().getWidgetSettings().getBoolean(SETTING_ENABLED));
        }
        if((!authorityWidgetSettingEnabled.isPresent() && getCockpitProperties().getBoolean(PROPERTY_ENABLED))
                        || (authorityWidgetSettingEnabled.isPresent() && authorityWidgetSettingEnabled.get()))
        {
            return super.decorateContext(context, configurationType, widgetInstance);
        }
        else
        {
            return context;
        }
    }


    @Required
    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
