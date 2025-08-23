/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.branding.customthemes.themes;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.model.ValidationResult;
import org.zkoss.zk.ui.Component;

public class ThemeValidationHelper
{
    private ValidationHandler validationHandler;


    protected void initValidation(final Component parent, final ThemesController controller, final Editor editor)
    {
        final var themeValidatable = new ThemeValidatable(controller, parent);
        editor.initValidation(themeValidatable, validationHandler);
    }


    protected void prepareValidationResultModel(final WidgetModel model)
    {
        var validationResultToSet = model.getValue(StandardModelKeys.VALIDATION_RESULT_KEY, ValidationResult.class);
        if(validationResultToSet == null)
        {
            validationResultToSet = new ValidationResult();
        }
        model.setValue(StandardModelKeys.VALIDATION_RESULT_KEY, validationResultToSet);
    }


    protected void clearValidationResultModel(final WidgetModel model)
    {
        model.setValue(StandardModelKeys.VALIDATION_RESULT_KEY, new ValidationResult());
    }


    public ValidationHandler getValidationHandler()
    {
        return validationHandler;
    }


    public void setValidationHandler(final ValidationHandler validationHandler)
    {
        this.validationHandler = validationHandler;
    }
}
