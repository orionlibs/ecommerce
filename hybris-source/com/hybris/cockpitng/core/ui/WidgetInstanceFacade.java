/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.ui;

import com.hybris.cockpitng.core.Widget;
import java.util.List;

/**
 * A facade for managing widget instances. This interface is designed to be used by the widget engine. Implementation
 * can contain lazy loading of widget instances and hide the details from the engine.
 */
public interface WidgetInstanceFacade
{
    /**
     * Returns a widget instance for the given root widget.
     * @param widget the widget from which root is returned
     * @return the root widget instance
     */
    WidgetInstance getRootWidgetInstance(Widget widget);


    /**
     * Returns a list of all child widget instances of the given parent.
     * @param parent WidgetInstance based on which child instances are returned
     * @return child widget instances
     */
    List<WidgetInstance> getWidgetInstances(WidgetInstance parent);


    /**
     * Returns a list of all instances of the given widget belonging to the given parent and assigned to the given
     * slotId.
     * @param isSingleSlot whether slot is single or not
     * @param parent the WidgetInstance from which Widget Instances will be returned
     * @param slotId assigned slotId from which slot instances will be returned
     * @return list of all instances of the given widget belonging to the given parent and assigned to the given slotId
     */
    List<WidgetInstance> getWidgetInstances(WidgetInstance parent, String slotId, boolean isSingleSlot);


    /**
     * Returns a list of all instances of the given widget belonging to the given parent.
     * @param parent the WidgetInstance from which Widget Instances will be returned
     * @param widget the parent from which instances will be returned
     * @return child widget instances
     */
    List<WidgetInstance> getWidgetInstances(Widget widget, WidgetInstance parent);


    /**
     * Creates new widget instance as a child of the given parent.
     * @param parent Widget Instance in which new widget instance will be created
     * @param widget a widget to create widget instance
     * @return created Widget Instance
     */
    WidgetInstance createWidgetInstance(Widget widget, WidgetInstance parent);


    /**
     * Creates new widget instance as a child of the given parent with assigned creator.
     * @param creator assigned creator
     * @param parentInstance Widget Instance in which new widget instance will be created
     * @param widget a widget to create widget instance
     * @return created Widget Instance
     */
    WidgetInstance createWidgetInstance(final Widget widget, final WidgetInstance parentInstance, final Object creator);


    /**
     * Removes given widget instance from the widget tree. View model will be removed as well. All child instances will
     * be removed as well.
     * @param instance Widget Instance to be removed form the widget tree
     */
    void removeWidgetInstance(WidgetInstance instance);


    /**
     * Checks if a new instance of the given widget can be created. It might be disabled by user visibility rule.
     *
     * @param widget
     *           check will be done if this widget can be created
     * @param parentInstance
     *           Widget Instance in which creation possibility will be tested
     * @return true if a given widget instance can be created
     */
    boolean canCreateInstance(Widget widget, WidgetInstance parentInstance);


    /**
     * Checks if the given instance can be removed.
     * @param instance test if this instance can be removed
     * @return true if removal is possible
     */
    boolean canRemoveInstance(WidgetInstance instance);


    /**
     * Returns a list of all possible widgets that instances can be created of.
     * @param parentInstance Widget Instance in which Widgets could be possibly created
     * @param slotId slotId name in which Widgets could be possibly created
     * @return a list of all possible widgets that instances can be created of
     */
    List<Widget> getPossibleWidgets(WidgetInstance parentInstance, String slotId);
}
