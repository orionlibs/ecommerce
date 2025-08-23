/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.hybris.cockpitng.components.Widgetslot;

/**
 * Service for grouping and ungrouping widgets.
 */
public interface WidgetGroupingService
{
    /**
     * Creates a composed widget out of the widget attached to the specified widgetslot.
     * Also replaces the specified widget with the newly created composed widget.
     *
     * @param widgetslot to add a widget
     */
    void groupWidget(Widgetslot widgetslot);


    /**
     * Replaces the composed widget attached to the specifed widgetslot with it's decompiled widget tree.
     *
     * @param widgetslot to add a widget
     */
    void ungroupWidget(Widgetslot widgetslot);
}
