/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.ui;

import com.hybris.cockpitng.core.Widget;
import java.util.List;

/**
 * Service for managing widget instances.
 *
 * @see WidgetInstance
 */
public interface WidgetInstanceService
{
    /**
     * Creates new widget instance for the given root widget.
     *
     * @param widget
     *           a widget to create widget instance
     * @return created widget instance
     */
    WidgetInstance createRootWidgetInstance(Widget widget);


    /**
     * Creates new widget instance as a child of the given parent.
     *
     * @param widget
     *           a widget to create widget instance
     * @param parent
     *           parent widget instance
     * @return created widget instance
     */
    WidgetInstance createWidgetInstance(Widget widget, WidgetInstance parent);


    /**
     * Creates new widget instance as a child of the given parent with assigned creator.
     *
     * @param widget
     *           a widget to create widget instance
     * @param parent
     *           parent widget instance
     * @param creator
     *           assigned creator
     * @return created widget instance
     */
    WidgetInstance createWidgetInstance(Widget widget, WidgetInstance parent, Object creator);


    /**
     * Returns a list of all widget instances of the given root widget.
     *
     * @param widget
     *           a widget for which a list of widget instances should be returned
     * @return a list of widget instances for the given widget
     */
    List<WidgetInstance> getRootWidgetInstances(Widget widget);


    /**
     * Returns a list of all child widget instances of the given parent.
     *
     * @param parent
     *           widget instance for which a list of child widget instances should be returned
     * @return a list of child widget instances for the given parent widget instance
     */
    List<WidgetInstance> getWidgetInstances(WidgetInstance parent);


    /**
     * Returns a list of all instances of the given widget belonging to the given parent.
     *
     * @param widget
     *           a root widget which instances should be returned
     * @param parent
     *           a parent widget instance for returned list of widget instances
     * @return a list of all instances of the given widget belonging to the given parent
     */
    List<WidgetInstance> getWidgetInstances(Widget widget, WidgetInstance parent);


    /**
     * Removes given widget instance from the widget tree. View model will be removed as well. All child instances will be
     * removed as well.
     *
     * @param instance
     *           widget instance to remove
     */
    void removeWidgetInstance(WidgetInstance instance);
}
