/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;

/**
 * Widget controller interface. The controller specified in a widget definition should implement it.
 */
public interface WidgetController
{
    TypedSettingsMap getWidgetSettings();


    WidgetModel getModel();
}
