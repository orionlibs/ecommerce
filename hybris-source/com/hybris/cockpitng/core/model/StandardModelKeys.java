/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model;

/**
 * Gathers all standard keys for widget model
 */
public interface StandardModelKeys
{
    /**
     * Default name of attribute for results of validation
     * <P>
     * Expected value: {@link com.hybris.cockpitng.validation.model.ValidationResult}
     */
    String VALIDATION_RESULT_KEY = "validationResult";
    /**
     * Default name of attribute for context on which a widget working
     */
    String CONTEXT_OBJECT = "currentObject";
    /**
     * Key for validation css class
     */
    String VALIDATION_SCLASS = "validationClass";
}