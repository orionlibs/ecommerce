/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.impl;

import com.hybris.cockpitng.validation.LocalizationAwareValidationHandler;
import com.hybris.cockpitng.validation.LocalizedQualifier;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Interface introducing validation handling for given object and qualifiers or localized qualifiers.
 */
public class DefaultLocalizationAwareValidationHandler extends DefaultValidationHandler
                implements LocalizationAwareValidationHandler
{
    @Override
    public List<ValidationInfo> validate(final Object objectToValidate, final Collection<LocalizedQualifier> qualifiers,
                    final ValidationContext validationContext)
    {
        return Collections.emptyList();
    }
}
