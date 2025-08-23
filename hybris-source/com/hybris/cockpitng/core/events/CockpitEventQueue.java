/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.events;

import java.util.List;

/**
 * The cockpit event queue gives the ability to process system events and convert them to cockpit socket events.
 * They can be connected to normal widgets using EventAcceptorWidgets. It is also possible to publish events from a widget using the EventProducerWidget.
 */
public interface CockpitEventQueue
{
    /**
     * Adds an event to the event queue.
     *
     * @param event The event to publish.
     */
    void publishEvent(CockpitEvent event);


    /**
     * Registers the widget as a listener for the specified event.
     *
     * @param widgetID A string that represents the widget.
     * @param eventName A string that defines the event.
     */
    void registerListener(String widgetID, String eventName, String scope);


    /**
     * Remove the specified widget from the set of listeners.
     * If the widget is not registered, this method does nothing.
     *
     * @param widgetID The widget to remove from the listeners.
     */
    void removeListener(String widgetID);


    /**
     * Returns all events available for the specified widget and removes them from the queue.
     *
     * @param widgetID The widget for which the events should be fetched.
     * @return The events being published since the last call of this method or an empty list, if the widget has not been registered.
     */
    List<CockpitEvent> fetchEvents(String widgetID);
}
