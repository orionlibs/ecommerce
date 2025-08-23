/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.impl;

import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation - does nothing will be overridden in upstream module.
 */
public class DefaultValidationHandler implements ValidationHandler
{
    @Override
    public List<ValidationInfo> validate(final Object objectToValidate, final ValidationContext validationContext)
    {
        return Collections.emptyList();
    }


    @Override
    public List<ValidationInfo> validate(final Object objectToValidate, final List<String> qualifiers, final ValidationContext validationContext)
    {
        return Collections.emptyList();
    }
}
