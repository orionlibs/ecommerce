/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.object;

/**
 * defines a comparator used by {@link ObjectComparatorService}.
 */
public interface ObjectComparator
{
    /**
     * Return true when two objects can be compare.
     */
    boolean canCompare(Object first, Object second);


    /**
     * Comparison result.
     */
    boolean equals(Object first, Object second);
}
