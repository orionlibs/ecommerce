/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

/**
 * API for renderers that could be used for rendering components within widgets extended with possibility to notify
 * listeners about rendering progress.
 *
 * @param <PARENT>
 *           type of parent component on which renderer is able to render
 * @param <CONFIG>
 *           type of configuration for renderer
 * @param <DATA>
 *           type of data that may be rendered
 * @see AbstractWidgetComponentRenderer
 */
public interface NotifyingWidgetComponentRenderer<PARENT, CONFIG, DATA> extends WidgetComponentRenderer<PARENT, CONFIG, DATA>
{
    /**
     * Registers a listener for any component rendering.
     *
     * @param listener
     *           listener to be registered
     */
    @Override
    void addRendererListener(final WidgetComponentRendererListener<PARENT, CONFIG, DATA> listener);


    /**
     * Unregisters a listener for any component rendering.
     *
     * @param listener
     *           listener to be registered
     */
    @Override
    void removeRendererListener(final WidgetComponentRendererListener<PARENT, CONFIG, DATA> listener);
}
