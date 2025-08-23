/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

/**
 * Listeners for renderer to render components
 */
public interface WidgetComponentRendererListener<P, C, D>
{
    /**
     * Informs that a component has been properly rendered.
     * <P>
     * Method is invoked for top-most component but also for all children that are rendered by this renderer.
     *
     * @param event
     *           details about render
     */
    void componentRendered(WidgetComponentRendererEvent<P, C, D> event);
}
