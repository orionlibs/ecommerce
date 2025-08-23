/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

import com.hybris.cockpitng.core.util.Validate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.zkoss.zk.ui.Component;

public abstract class AbstractWidgetComponentRenderer<PARENT, CONFIG, DATA> implements NotifyingWidgetComponentRenderer<PARENT, CONFIG, DATA>
{
    private final List<WidgetComponentRendererListener<PARENT, CONFIG, DATA>> listeners = Collections.synchronizedList(new ArrayList<>());


    @Override
    public void addRendererListener(final WidgetComponentRendererListener<PARENT, CONFIG, DATA> listener)
    {
        synchronized(listeners)
        {
            listeners.add(0, listener);
        }
    }


    @Override
    public void removeRendererListener(final WidgetComponentRendererListener<PARENT, CONFIG, DATA> listener)
    {
        synchronized(listeners)
        {
            listeners.remove(listener);
        }
    }


    /**
     * Notifies all listeners that rendering of parent has been finished and no other notifications will be triggered
     * in regards to this component until next rendering is invoked.
     *
     * @param parent parent component on which object has been rendered
     * @param config renderer configuration
     * @param data data to be rendered
     */
    public void fireComponentRendered(final PARENT parent, final CONFIG config, final DATA data)
    {
        final WidgetComponentRendererEvent<PARENT, CONFIG, DATA> event = new WidgetComponentRendererEvent<>((Component)parent, this);
        event.setEventData(parent, config, data);
        fireComponentRendered(event);
    }


    /**
     * Notified all listeners that some child component has been rendered on specified parent. It is not required that
     * provided parent is directly over a child component, yet child needs to be below parent in regards to component
     * hierarchy tree.
     *
     * @param component child component that has been rendered
     * @param parent parent component on which rendering is requested
     * @param config renderer configuration
     * @param data data that is being rendered
     */
    public void fireComponentRendered(final Component component, final PARENT parent, final CONFIG config, final DATA data)
    {
        final WidgetComponentRendererEvent<PARENT, CONFIG, DATA> event = new WidgetComponentRendererEvent<>(component, this);
        event.setEventData(parent, config, data);
        fireComponentRendered(event);
    }


    /**
     * Notifies all listeners about rendering event.
     *
     * @param event event to be broadcasted
     */
    public void fireComponentRendered(final WidgetComponentRendererEvent<PARENT, CONFIG, DATA> event)
    {
        Validate.assertTrue("Unable to fire event for different renderer", event.getRenderer() == this);
        final List<WidgetComponentRendererListener<PARENT, CONFIG, DATA>> widgetListeners;
        synchronized(this.listeners)
        {
            widgetListeners = Arrays.asList(this.listeners.toArray(new WidgetComponentRendererListener[0]));
        }
        widgetListeners.forEach(listener -> listener.componentRendered(event));
    }
}
