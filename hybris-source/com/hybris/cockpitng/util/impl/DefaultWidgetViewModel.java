/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl;

import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.WidgetInstanceManagerAware;

/**
 *
 */
public class DefaultWidgetViewModel implements WidgetInstanceManagerAware
{
    private WidgetInstanceManager widgetInstanceManager;


    @Override
    public void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }


    public TypedSettingsMap getWidgetSettings()
    {
        return widgetInstanceManager.getWidgetSettings();
    }


    public WidgetModel getWidgetModel()
    {
        return widgetInstanceManager.getModel();
    }


    protected void sendSocketOutput(final String socketID, final Object data)
    {
        widgetInstanceManager.sendOutput(socketID, data);
    }


    protected WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }
}
