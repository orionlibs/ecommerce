/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.hybris.cockpitng.validation.ValidationHandler;

/**
 *
 *
 */
public class DefaultEditorValidationFactory implements EditorValidationFactory
{
    @Override
    public EditorValidation createValidation(final String path)
    {
        return new DefaultEditorValidation(path);
    }


    @Override
    public EditorValidation createValidation(final String path, final ValidationHandler validationHandler)
    {
        return new DefaultEditorValidation(path, validationHandler);
    }
}
