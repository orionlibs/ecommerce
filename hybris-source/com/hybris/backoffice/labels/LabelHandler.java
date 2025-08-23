/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.labels;

/**
 * This interface allows to obtain labels for objects using transformation object.
 * @param <VALUE>
 *           this generic argument represents the type of value to be transformed.
 * @param <MODIFIER>
 *           this generic argument represents the type of object used to define the transformation of the passed-in
 *           object.
 */
public interface LabelHandler<VALUE, MODIFIER>
{
    /**
     *
     * @param value object to be labelled with the {@param modifier}.
     * @param modifier object used to define how the label should be calculated.
     * @return a string that represents the passed-in object using the given modifier.
     */
    String getLabel(final VALUE value, final MODIFIER modifier);
}
