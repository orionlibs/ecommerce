/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels;

import org.apache.commons.lang3.StringUtils;

/**
 * Service for extracting labels (label, description and icon path) for a given Object.
 */
public interface LabelService
{
    /**
     * Returns label for a given Object.
     *
     * @param object target object
     * @return label for a given object
     */
    String getObjectLabel(Object object);


    /**
     * Returns short label for a given Object. By default delegates to {@link #getObjectLabel(Object)}.
     *
     * @param object target object
     * @return label for a given object
     * @see #getObjectLabel
     */
    default String getShortObjectLabel(final Object object)
    {
        return getObjectLabel(object);
    }


    /**
     * Returns text description for a given Object.
     *
     * @param object target object
     * @return description for a given object
     */
    String getObjectDescription(Object object);


    /**
     * Returns icon path for a given Object.
     *
     * @param object target object
     * @return icon path for a given object
     */
    String getObjectIconPath(Object object);


    /**
     * Returns a message informing that access is denied for specified object
     *
     * @param object target object
     * @return access-denied message
     */
    default String getAccessDeniedLabel(final Object object)
    {
        return StringUtils.EMPTY;
    }


    /**
     * Returns a message informing that specified object value for specific language is disabled
     *
     * @param object
     *           target object
     * @return disabled for language message
     */
    default String getLanguageDisabledLabel(final Object object)
    {
        return StringUtils.EMPTY;
    }
}
