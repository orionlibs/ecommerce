/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels;

/**
 * Provider for essence data to display (label, description, icon) for a given Object.
 */
public interface LabelProvider<T>
{
    /**
     * Returns label for a given Object.
     *
     * @param object
     * @return label for a given object
     */
    String getLabel(T object);


    /**
     * Returns shortLabel for a given Object. By default delegates to {@link #getLabel(Object)}
     *
     * @param object
     * @return short label for a given object
     */
    default String getShortLabel(final T object)
    {
        return getLabel(object);
    }


    /**
     * Returns text description for a given Object.
     *
     * @param object
     * @return description for a given object
     */
    String getDescription(T object);


    /**
     * Returns icon path for a given Object.
     *
     * @param object
     * @return icon path for a given object
     */
    String getIconPath(T object);
}
