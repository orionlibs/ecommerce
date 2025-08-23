/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.validation.BackofficeValidationService;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import java.util.List;

/**
 * Platform specific implementation of {@link ValidationHandler} that delegates to {@link BackofficeValidationService}.
 */
public class BackofficeValidationHandler implements ValidationHandler
{
    private BackofficeValidationService validationService;


    public BackofficeValidationService getValidationService()
    {
        return validationService;
    }


    public void setValidationService(final BackofficeValidationService validationService)
    {
        this.validationService = validationService;
    }


    @Override
    public List<ValidationInfo> validate(final Object o, final ValidationContext validationContext)
    {
        return validationService.validate(o, validationContext);
    }


    @Override
    public List<ValidationInfo> validate(final Object o, final List<String> list, final ValidationContext validationContext)
    {
        return validationService.validate(o, list, validationContext);
    }
}
