/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Cleanable;
import com.hybris.cockpitng.editors.EditorContext;

/**
 *
 *
 */
public interface EditorValidation extends Cleanable
{
    String ON_VALIDATION_CHANGED = "onValidationChanged";


    <T> void init(final ValidatableContainer validatableContainer, final Editor editor, final EditorContext<T> editorContext);


    void editorRendered();


    void editorValidationChanged();
}
