/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

/**
 * Interface used to implement specific behaviour of html focus
 *
 */
public interface Focusable
{
    void focus();


    /**
     * Used in localized editor, should perform also scrollIntoView, because focus handling in editor area does not scroll when this method is used.
     *
     */
    default void focus(final String path)
    {
        focus();
    }
}
