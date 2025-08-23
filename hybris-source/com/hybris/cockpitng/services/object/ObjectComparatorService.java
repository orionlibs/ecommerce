/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.object;

/**
 * Allows to define comparators for particular objects that have not implemented correct equals.
 */
public interface ObjectComparatorService
{
    /**
     * Allows to check if the given objects are equal
     *
     * @param first object to compare
     * @param second object to compare
     * @return whether the given objects are equal or not
     */
    boolean equals(Object first, Object second);
}
