/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.renderer;

import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.configurableflow.validation.ValidationAwareCustomViewRenderer;
import java.util.Map;
import org.zkoss.zk.ui.Component;

public abstract class DefaultValidatableCustomViewRenderer
                extends AbstractWidgetComponentRenderer<Component, ViewType, Map<String, String>> implements
                ValidationAwareCustomViewRenderer
{
    @Override
    public abstract void render(final Component parent, ValidatableContainer validatableContainer, final ViewType customView,
                    final Map<String, String> parameters, final DataType dataType, final WidgetInstanceManager widgetInstanceManager);
}
