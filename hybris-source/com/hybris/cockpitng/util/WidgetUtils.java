/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * Utility methods for cockpitng framework engine.
 * For internal use only.
 */
public interface WidgetUtils
{
    String NOTIFIER_STACK = "notifierStack";


    void refreshWidgetLibrary();


    default void clearWidgetLibrary()
    {
        // NOOP
    }


    /**
     * @param content content component of the notify entry.
     * @return returns a component that represents a notifier entry which will be created in the root notifier component.
     *         This component should be detached if not used
     *         anymore.
     */
    Component addNotifierToStack(Component content);


    /**
     * @param content content component of the notify entry
     * @param notifierStack All notifiers will be displayed in this component. If null, no notifier entries will be
     *           created at all.
     * @return returns a component that represents a notifier entry. This component should be detached if not used
     *         anymore.
     */
    Component addNotifierToStack(Component content, Component notifierStack);


    /**
     * Gets the root of the current desktop.
     */
    Component getCockpitRoot();


    /**
     * Checks, if the given widget is has a group widget as ancestor.
     */
    boolean isPartOfComposedWidget(Widget widget);


    /**
     * Adds an event listener to the global event queue.
     */
    void addGlobalEventListener(String eventName, Widgetslot widget, EventListener<Event> eventListener, String scope);


    /**
     * Gets all unprocessed events from the global event queue and notifies subscribers.
     */
    void dispatchGlobalEvents() throws Exception;


    /**
     * Same as {@link #getCockpitRoot()}.
     */
    Component getRoot();


    /**
     * Gets the {@link Widgetslot} component for the given {@link WidgetInstance}, if it has been registered for the
     * current {@link Desktop}.
     */
    Widgetslot getRegisteredWidgetslot(WidgetInstance widgetInstance);


    /**
     * Gets the {@link Widgetslot} component for the given widgetInstanceId, if it has been registered for the
     * current {@link Desktop}.
     */
    Widgetslot getRegisteredWidgetslot(String widgetInstanceId);


    /**
     * Registers a {@link Widgetslot} at the current {@link Desktop}.
     */
    void registerWidgetslot(Widgetslot widgetslot);
}
