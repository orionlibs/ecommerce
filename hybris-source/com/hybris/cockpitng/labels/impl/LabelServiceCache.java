/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import java.util.function.Supplier;

/**
 * A cache for label service to avoid multiple label resolving in single request.
 */
public interface LabelServiceCache
{
    /**
     * Returns label for a given Object.
     *
     * @param object
     *           target object
     * @param defaultValue
     *           supplier for default value, if cache does not have one
     * @return label for a given object
     */
    String getObjectLabel(Object object, final Supplier<String> defaultValue);


    /**
     * Returns short label for a given Object.
     *
     * @param object
     *           target object
     * @param defaultValue
     *           supplier for default value, if cache does not have one
     * @return short label for a given object
     * @see #getObjectLabel
     */
    String getShortObjectLabel(final Object object, final Supplier<String> defaultValue);


    /**
     * Returns text description for a given Object.
     *
     * @param object
     *           target object
     * @param defaultValue
     *           supplier for default value, if cache does not have one
     * @return description for a given object
     */
    String getObjectDescription(Object object, final Supplier<String> defaultValue);


    /**
     * Returns icon path for a given Object.
     *
     * @param object
     *           target object
     * @param defaultValue
     *           supplier for default value, if cache does not have one
     * @return icon path for a given object
     */
    String getObjectIconPath(Object object, final Supplier<String> defaultValue);
}
