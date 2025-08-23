/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.renderers.common;

import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import org.zkoss.zk.ui.Component;

/**
 * Defines renderer contract that is used for displaying a single setting.
 */
public interface TypedSettingsRenderer
{
    /**
     * Creates a representation of given setting.
     *
     * @param widgetSettings settings container
     * @param settingKey given setting
     * @param hasConnections whether parent widget has connections
     *
     * @return a component representation of given setting
     */
    Component renderSetting(final TypedSettingsMap widgetSettings, final String settingKey, final boolean hasConnections);
}
