/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.perspectivechooser.renderer;

import com.hybris.backoffice.navigation.bar.NavigationContext;
import org.zkoss.zk.ui.Component;

/**
 * Renderer for possible perspectives on
 * {@link com.hybris.backoffice.widgets.perspectivechooser.controller.PerspectiveChooserWidgetController}'s toolbar
 *
 */
public interface PerspectiveChooserRenderer
{
    /**
     * Renders
     * {@link com.hybris.backoffice.widgets.perspectivechooser.controller.PerspectiveChooserWidgetController}'s toolbar
     *
     * @param parent toolbar component
     * @param context chooser's context
     */
    void renderTree(final Component parent, final NavigationContext context);


    /**
     * Method informs renderer that selection of perspective has changed from other source then users action on elements
     * rendered by this renderer. Renderer should perform any needed actions so that all elements on
     * {@link com.hybris.backoffice.widgets.perspectivechooser.controller.PerspectiveChooserWidgetController}'s toolbar
     * are rendered properly in regards of new selection.
     *
     * @param parent toolbar component
     * @param context chooser's context
     */
    void updatePerspectiveSelection(final Component parent, final NavigationContext context);
}
