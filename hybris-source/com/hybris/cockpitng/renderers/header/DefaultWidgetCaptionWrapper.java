/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.renderers.header;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link WidgetCaptionWrapper}.
 */
public class DefaultWidgetCaptionWrapper implements WidgetCaptionWrapper
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetCaptionWrapper.class);
    private final Map<String, WidgetCaptionEventListener> listeners = new HashMap<String, WidgetCaptionEventListener>();
    private final WidgetCaptionEventListener containerListener;
    private final Map<String, Boolean> stateMap = new HashMap<String, Boolean>(4);
    private int hiddenContainerControls;
    private boolean maximizable;
    private boolean minimizable;
    private boolean closable;
    private boolean collapsible;


    public DefaultWidgetCaptionWrapper(final WidgetCaptionEventListener containerListener, final boolean focused,
                    final boolean minimized, final boolean maximized, final boolean collapsed)
    {
        this.containerListener = containerListener;
        stateMap.put(ON_WIDGET_MINIMIZE, Boolean.valueOf(minimized));
        stateMap.put(ON_WIDGET_MAXIMIZE, Boolean.valueOf(maximized));
        stateMap.put(ON_WIDGET_FOCUS, Boolean.valueOf(focused));
        stateMap.put(ON_WIDGET_COLLAPSE, Boolean.valueOf(collapsed));
    }


    @Override
    public void hideContainerControls(final int controls)
    {
        hiddenContainerControls = controls;
    }


    public int getHiddenContainerControls()
    {
        return hiddenContainerControls;
    }


    public void sendEvent(final String eventName, final boolean state)
    {
        if(eventName != null)
        {
            stateMap.put(eventName, Boolean.valueOf(state));
            final WidgetCaptionEventListener widgetCaptionEventListener = listeners.get(eventName);
            if(widgetCaptionEventListener != null)
            {
                widgetCaptionEventListener.onEvent(eventName, this);
            }
        }
        else
        {
            LOG.error("Event name must not be null.");
        }
    }


    @Override
    public void addListener(final String eventName, final WidgetCaptionEventListener eventListener)
    {
        listeners.put(eventName, eventListener);
    }


    @Override
    public boolean isMinimized()
    {
        return stateMap.get(ON_WIDGET_MINIMIZE);
    }


    @Override
    public boolean isMaximized()
    {
        return stateMap.get(ON_WIDGET_MAXIMIZE);
    }


    @Override
    public boolean isFocused()
    {
        return stateMap.get(ON_WIDGET_FOCUS);
    }


    @Override
    public boolean isCollapsed()
    {
        return stateMap.get(ON_WIDGET_COLLAPSE);
    }


    protected void notifyContainerListener(final String eventName)
    {
        containerListener.onEvent(eventName, this);
    }


    @Override
    public void setMinimized(final boolean minimized)
    {
        if(isMinimizable())
        {
            stateMap.put(ON_WIDGET_MINIMIZE, Boolean.valueOf(minimized));
            notifyContainerListener(ON_WIDGET_MINIMIZE);
        }
    }


    @Override
    public void setMaximized(final boolean maximized)
    {
        if(isMaximizable())
        {
            stateMap.put(ON_WIDGET_MAXIMIZE, Boolean.valueOf(maximized));
            notifyContainerListener(ON_WIDGET_MAXIMIZE);
        }
    }


    @Override
    public void setFocused(final boolean focused)
    {
        stateMap.put(ON_WIDGET_FOCUS, Boolean.valueOf(focused));
        notifyContainerListener(ON_WIDGET_FOCUS);
    }


    @Override
    public void setCollapsed(final boolean collapsed)
    {
        if(isCollapsible())
        {
            stateMap.put(ON_WIDGET_COLLAPSE, Boolean.valueOf(collapsed));
            notifyContainerListener(ON_WIDGET_COLLAPSE);
        }
    }


    @Override
    public void close()
    {
        if(isClosable())
        {
            notifyContainerListener(ON_WIDGET_CLOSE);
        }
    }


    @Override
    public boolean isMaximizable()
    {
        return maximizable;
    }


    @Override
    public boolean isMinimizable()
    {
        return minimizable;
    }


    @Override
    public boolean isClosable()
    {
        return closable;
    }


    @Override
    public boolean isCollapsible()
    {
        return collapsible;
    }


    public void setMaximizable(final boolean maximizable)
    {
        this.maximizable = maximizable;
    }


    public void setMinimizable(final boolean minimizable)
    {
        this.minimizable = minimizable;
    }


    public void setClosable(final boolean closable)
    {
        this.closable = closable;
    }


    public void setCollapsible(final boolean collapsible)
    {
        this.collapsible = collapsible;
    }
}
