/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import com.hybris.cockpitng.core.ui.WidgetInstance;

/**
 * Service delivering user permissions for widget instances.
 */
public interface WidgetInstancePermissionService
{
    /**
     * Checks if it is possible to create a new instance of the given widget as a child of the given parent instance
     * according to rules.
     *
     * @param widget
     *           the widget to create instances for
     * @param parentInstance
     *           the parent instance of the to-be-created instance
     * @return true if possible to create new instance according to rules of false otherwise
     */
    boolean canCreateInstance(Widget widget, WidgetInstance parentInstance);


    /**
     * Checks if it is possible to remove given widget instance of the given widget according to rules.
     *
     * @param instance the widget instance to be removed
     * @return true if possible to remove the instance according to rules of false otherwise
     */
    boolean canRemoveInstance(WidgetInstance instance);
}
