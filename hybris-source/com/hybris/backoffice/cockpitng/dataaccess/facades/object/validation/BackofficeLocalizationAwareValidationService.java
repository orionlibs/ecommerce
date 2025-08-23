/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.object.validation;

import com.hybris.cockpitng.validation.LocalizedQualifier;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import java.util.Collection;
import java.util.List;

/**
 * {@inheritDoc} Additionally the service supports validation for properties of object for only selected set of locales.
 */
public interface BackofficeLocalizationAwareValidationService extends BackofficeValidationService
{
    /**
     * Validate qualifiers for given object, for given locales, with defined validation context {@link ValidationContext}.
     *
     * @param objectToValidate
     *           object to validate.
     * @param localizedQualifiers
     *           qualifiers.
     * @param validationContext
     *           validation context.
     * @return list of {@link ValidationInfo} containing validation results
     */
    List<ValidationInfo> validate(Object objectToValidate, Collection<LocalizedQualifier> localizedQualifiers,
                    ValidationContext validationContext);
}
