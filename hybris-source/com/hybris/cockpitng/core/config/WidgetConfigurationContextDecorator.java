/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

import com.hybris.cockpitng.core.ui.WidgetInstance;

/**
 * Provides additional context for the configuration.
 */
public interface WidgetConfigurationContextDecorator
{
    <C> ConfigContext decorateContext(ConfigContext context, Class<C> configurationType, final WidgetInstance widgetInstance);
}
