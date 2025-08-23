/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import org.zkoss.zk.ui.Component;

/**
 * The purpose of this interface is to render widget toolbar
 */
public interface WidgetToolbarRenderer
{
    void setInvisibleContainerVisible(final Widgetslot widgetSlot, final boolean value);


    void renderShowInvisibleChildrenButton(final Component parent, final Widgetslot widgetSlot);


    void appendWidgetToolbar(final Component parent, final WidgetDefinition widgetDefinition, final Widgetslot widgetSlot);


    void appendSettingsButton(final Component parent, final Widgetslot widgetSlot);


    void renderRemoveBtn(final Component parent, final Widgetslot widgetSlot, final WidgetDefinition widgetDefinition);


    void renderComposedGroupButton(final Component parent, final Widgetslot widgetSlot);


    boolean confirmNeeded(final Widget widget);


    void removeWidget(final Widgetslot widgetSlot, final Widget widget);
}
