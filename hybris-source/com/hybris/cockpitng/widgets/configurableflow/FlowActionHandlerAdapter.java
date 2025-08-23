/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow;

import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 *
 * Useful adapter that is used for {@link com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler} in order to
 * control
 * configurable flow widget.
 *
 **/
public abstract class FlowActionHandlerAdapter
{
    private final WidgetInstanceManager widgetInstanceManager;


    public FlowActionHandlerAdapter(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }


    /**
     * Closes current configurable flow widget instance.
     */
    public abstract void cancel();


    /**
     * Perform done operation for current configurable flow widget instance.
     */
    public abstract void done();


    /**
     * Perform back operation for current configurable flow widget instance.
     */
    public abstract void back();


    /**
     * Perform next operation for current configurable flow widget instance.
     */
    public abstract void next();


    /**
     * Perform custom operation for current configurable flow widget instance.
     */
    public abstract void custom();
}
