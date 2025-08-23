/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine;

import com.hybris.cockpitng.core.ui.WidgetInstance;

/**
 *
 */
public interface SessionWidgetInstanceRegistry
{
    WidgetInstance getRegisteredWidgetInstance(String slotId);


    void registerWidgetInstance(WidgetInstance instance, String slotId);


    void unregisterWidgetInstance(String slotId);


    void clear();
}
