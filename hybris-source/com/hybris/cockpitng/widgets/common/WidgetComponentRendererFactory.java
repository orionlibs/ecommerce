/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

import org.zkoss.zk.ui.Component;

public interface WidgetComponentRendererFactory<I extends Component, C, D>
{
    WidgetComponentRenderer<I, C, D> createWidgetComponentRenderer(final String configuredRenderer);
}
