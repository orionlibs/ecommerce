/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 * API for renderers that could be used for rendering components within widgets.
 *
 * @param <PARENT>
 *           type of parent component on which renderer is able to render
 * @param <CONFIG>
 *           type of configuration for renderer
 * @param <DATA>
 *           type of data that may be rendered
 */
public interface WidgetComponentRenderer<PARENT, CONFIG, DATA>
{
    /**
     * Registers a listener for any component rendering.
     *
     * @param listener
     *           listener to be registered
     * @deprecated since 6.5
     * @see NotifyingWidgetComponentRenderer
     */
    @Deprecated(since = "6.5", forRemoval = true)
    default void addRendererListener(final WidgetComponentRendererListener<PARENT, CONFIG, DATA> listener)
    {
    }


    /**
     * Unregisters a listener for any component rendering.
     *
     * @param listener
     *           listener to be registered
     * @deprecated since 6.5
     * @see NotifyingWidgetComponentRenderer
     */
    @Deprecated(since = "6.5", forRemoval = true)
    default void removeRendererListener(final WidgetComponentRendererListener<PARENT, CONFIG, DATA> listener)
    {
    }


    /**
     * Renders a component inside <b>parent</b>, representing given <b>data</b> and complying to given ui
     * <b>configuration</b>. The data should be of give <b>dataType</b>. The renderer implementation could communicate with
     * underlying widget via <b>widgetInstanceManager</b>.
     *
     * @param parent
     *           parent component on which to render
     * @param configuration
     *           configuration of renderer
     * @param data
     *           data to be rendered
     * @param dataType
     *           meta information about type of data provided
     * @param widgetInstanceManager
     *           widget manager in scope of which renderer is used
     */
    void render(PARENT parent, CONFIG configuration, DATA data, DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager);


    /**
     * Renders a component inside <b>parent</b>, representing given <b>data</b> and complying to given ui
     * <b>configuration</b>. The data should be of give <b>dataType</b>. The renderer implementation could communicate with
     * underlying widget via <b>widgetInstanceManager</b>.
     *
     * @param parent
     *           parent component on which to render
     * @param configuration
     *           configuration of renderer
     * @param data
     *           data to be rendered
     * @param dataType
     *           meta information about type of data provided
     * @param widgetInstanceManager
     *           widget manager in scope of which renderer is used
     * @param context
     *           context for the renderer
     * @deprecated since version 6.5, use
     *             {@linkplain #render(java.lang.Object, java.lang.Object, java.lang.Object, com.hybris.cockpitng.dataaccess.facades.type.DataType, com.hybris.cockpitng.engine.WidgetInstanceManager)}
     *             instead
     */
    @Deprecated(since = "6.5", forRemoval = true)
    default void render(final PARENT parent, final CONFIG configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager, final Context context)
    {
        render(parent, configuration, data, dataType, widgetInstanceManager);
    }
}
