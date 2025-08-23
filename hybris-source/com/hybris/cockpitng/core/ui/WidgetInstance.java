/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.ui;

import com.hybris.cockpitng.core.Widget;
import java.io.Serializable;

/**
 * Represents an instance of a widget in the view. There can be more than one widget instance for a single widget.
 */
public interface WidgetInstance extends Serializable
{
    /**
     * @return The widget model for this instance
     */
    Object getModel();


    /**
     * A string with position info. Can be used e.g. by a container renderer to render a child widget at a defined place.
     */
    String getPositionInfo();


    /**
     * @return The id of this instance, usually the id of the {@link Widget} plus a suffix.
     */
    String getId();


    // TODO this must be per-slotID (or store as a flag on the child)
    int getSelectedChildIndex();


    void setSelectedChildIndex(int index);


    /**
     * @return The parent instance.
     */
    WidgetInstance getParent();


    /**
     * @return The corresponding {@link Widget}.
     */
    Widget getWidget();


    /**
     * @return The object which triggered the instance creation.
     */
    Object getCreator();


    /**
     * @return The template ancestor instance, if existing or null otherwise.
     */
    WidgetInstance getTemplateRoot();
}
