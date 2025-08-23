/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.type;

/**
 * Service that handle values access by SpEL language.
 */
public interface ObjectValueService
{
    /**
     * Gets value of field from object accessed by given SpEL expression
     *
     * @param expression SpEL expression
     * @param object to access
     * @return a value of field from object accessed by given SpEL expression
     */
    <T> T getValue(final String expression, final Object object);


    /**
     * Sets the given value to a field of object accessed by given SpEL expression
     *
     * @param expression SpEL expression
     * @param object the object to apply the expression on
     * @param value the value to store
     */
    void setValue(final String expression, final Object object, final Object value);
}
