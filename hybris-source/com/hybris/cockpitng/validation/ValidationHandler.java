/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation;

import com.hybris.cockpitng.validation.model.ValidationInfo;
import java.util.List;

/**
 * <ul>
 * <li>Interface introducing validation handling for given object and qualifiers. Note.
 * The default implementation should return only empty list of {@link ValidationInfo}.</li>
 * </ul>
 */
public interface ValidationHandler
{
    default List<ValidationInfo> validate(final Object objectToValidate)
    {
        return validate(objectToValidate, (ValidationContext)null);
    }


    List<ValidationInfo> validate(final Object objectToValidate, final ValidationContext validationContext);


    /**
     * Validate qualifiers for given object with defined validation context {@link ValidationContext}
     *
     * @param objectToValidate
     *           the object to check
     * @param qualifiers
     *           to validate
     * @param validationContext
     *           specific validation context
     */
    List<ValidationInfo> validate(final Object objectToValidate, final List<String> qualifiers,
                    final ValidationContext validationContext);


    /**
     * Validate qualifiers for given object with defined validation context {@link ValidationContext}
     *
     * @param objectToValidate
     *           the object to check
     * @param qualifiers
     *           to validate
     */
    default List<ValidationInfo> validate(final Object objectToValidate, final List<String> qualifiers)
    {
        return validate(objectToValidate, qualifiers, null);
    }
}
