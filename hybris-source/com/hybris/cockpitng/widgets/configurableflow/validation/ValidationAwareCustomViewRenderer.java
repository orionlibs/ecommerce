/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.validation;

import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Map;
import java.util.Set;
import org.zkoss.zk.ui.Component;

/**
 * @deprecated since 1811, use {@link LocalizedValidationAwareCustomViewRenderer} instead
 */
@Deprecated(since = "1811", forRemoval = true)
public interface ValidationAwareCustomViewRenderer
{
    void render(final Component parent, ValidatableContainer validatableContainer, final ViewType customView,
                    final Map<String, String> parameters, final DataType dataType, final WidgetInstanceManager widgetInstanceManager);


    Set<String> getValidationProperties(WidgetInstanceManager wim, Map<String, String> params);
}
