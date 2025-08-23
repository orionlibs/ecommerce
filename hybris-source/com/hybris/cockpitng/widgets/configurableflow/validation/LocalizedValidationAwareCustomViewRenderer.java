/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.validation;

import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.validation.LocalizedQualifier;
import java.util.Collection;
import java.util.Map;
import org.zkoss.zk.ui.Component;

/**
 * CustomView renderer for ConfigurableFlow that aware of the localized parameters for validation.
 */
public interface LocalizedValidationAwareCustomViewRenderer
{
    void render(final Component parent, ValidatableContainer validatableContainer, final ViewType customView,
                    final Map<String, String> parameters, final DataType dataType, final WidgetInstanceManager widgetInstanceManager);


    /**
     * Fetches properties for validation along with their locales that are rendered by this renderer.
     *
     * @param wim
     *           Widget Instance Manager from the controller
     * @param params
     *           parameters passed to this renderer
     * @return collection of properties with their locales
     */
    Collection<LocalizedQualifier> getValidationPropertiesWithLocales(WidgetInstanceManager wim, Map<String, String> params);
}
