/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation;

import com.hybris.cockpitng.validation.model.ValidationInfo;
import java.util.Collection;
import java.util.List;

/**
 * {@inheritDoc}
 */
public interface LocalizationAwareValidationHandler extends ValidationHandler
{
    /**
     * Validate qualifiers for given locales, for given object, with defined validation context {@link ValidationContext}
     *
     * @param objectToValidate
     *           the object to check
     * @param qualifiers
     *           to validate
     * @param validationContext
     *           specific validation context
     * @return list of validation result
     */
    List<ValidationInfo> validate(Object objectToValidate, Collection<LocalizedQualifier> qualifiers,
                    ValidationContext validationContext);
}
