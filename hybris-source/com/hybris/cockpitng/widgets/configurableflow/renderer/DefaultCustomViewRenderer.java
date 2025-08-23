/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.renderer;

import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import java.util.Map;
import org.zkoss.zk.ui.Component;

public abstract class DefaultCustomViewRenderer extends AbstractWidgetComponentRenderer<Component, ViewType, Map<String, String>>
{
    @Override
    public abstract void render(final Component parent, final ViewType customView, final Map<String, String> parameters, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager);
}
