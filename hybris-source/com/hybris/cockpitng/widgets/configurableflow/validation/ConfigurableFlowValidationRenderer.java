/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.validation;

import com.hybris.cockpitng.common.renderer.TypeAwareValidationRenderer;
import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.validation.model.ValidationResult;
import java.util.function.BiConsumer;
import org.zkoss.zul.Listbox;

public class ConfigurableFlowValidationRenderer extends TypeAwareValidationRenderer
{
    public ConfigurableFlowValidationResultsPopup createValidationViolationsPopup(final ValidatableContainer container,
                    final BiConsumer<String, ValidationResult> popupCtrlActionConsumer)
    {
        final Listbox list = createValidationViolationsList(container);
        final ConfigurableFlowValidationResultsPopup popup = new ConfigurableFlowValidationResultsPopup(container, list, popupCtrlActionConsumer);
        return popup;
    }
}
