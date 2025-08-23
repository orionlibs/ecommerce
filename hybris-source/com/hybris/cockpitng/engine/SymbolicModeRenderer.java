/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.engine.impl.AdminmodeWidgetEngine;

/**
 * Renderer for the symbolic admin mode, used by {@link AdminmodeWidgetEngine}.
 */
public interface SymbolicModeRenderer
{
    /**
     * Renders the content of a widgetslot, according to the currently assigned widget.
     *
     * @param widgetslot The widgetslot to render.
     * @param engine The calling AdminmodeWidgetEngine instance.
     */
    void render(Widgetslot widgetslot, AdminmodeWidgetEngine engine);
}
