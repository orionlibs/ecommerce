/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.validation.BackofficeLocalizationAwareValidationService;
import com.hybris.cockpitng.validation.LocalizationAwareValidationHandler;
import com.hybris.cockpitng.validation.LocalizedQualifier;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 * Platform specific implementation of {@link ValidationHandler} and {@link LocalizationAwareValidationHandler} that
 * delegates to {@link BackofficeLocalizationAwareValidationService}.
 */
public class BackofficeLocalizationAwareValidationHandler implements LocalizationAwareValidationHandler
{
    private BackofficeLocalizationAwareValidationService validationService;


    @Override
    public List<ValidationInfo> validate(final Object objectToValidate, final ValidationContext validationContext)
    {
        return getValidationService().validate(objectToValidate, validationContext);
    }


    @Override
    public List<ValidationInfo> validate(final Object objectToValidate, final List<String> qualifiers,
                    final ValidationContext validationContext)
    {
        return getValidationService().validate(objectToValidate, qualifiers, validationContext);
    }


    @Override
    public List<ValidationInfo> validate(final Object objectToValidate,
                    final Collection<LocalizedQualifier> qualifiersWithLocales, final ValidationContext validationContext)
    {
        return getValidationService().validate(objectToValidate, qualifiersWithLocales, validationContext);
    }


    public BackofficeLocalizationAwareValidationService getValidationService()
    {
        return validationService;
    }


    @Required
    public void setValidationService(final BackofficeLocalizationAwareValidationService validationService)
    {
        this.validationService = validationService;
    }
}
