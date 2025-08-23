/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model;

/**
 * Object able to observe changes of some nested values
 *
 */
public interface ValueObserver
{
    /**
     * Will be called after some value of the model was changed.
     */
    void modelChanged();


    /**
     * Will be called after some value of the model was changed.
     *
     * @param property exact model property which has been changed.
     */
    default void modelChanged(final String property)
    {
        modelChanged();
    }
}
