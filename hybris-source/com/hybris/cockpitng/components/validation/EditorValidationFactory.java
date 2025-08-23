/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.hybris.cockpitng.validation.ValidationHandler;

/**
 *
 *
 */
public interface EditorValidationFactory
{
    EditorValidation createValidation(final String path);


    default EditorValidation createValidation(final String path, final ValidationHandler validationHandler)
    {
        return createValidation(path);
    }
}
