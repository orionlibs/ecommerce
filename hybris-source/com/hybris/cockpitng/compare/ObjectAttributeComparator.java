/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.compare;

/**
 * A comparator interface check if two objects are considered to be equal or not.
 */
public interface ObjectAttributeComparator<T>
{
    /**
     * Checks, if two objects are considered equal.
     *
     * @param object1
     *           first object
     * @param object2
     *           second object
     * @return true, if equal, false otherwise
     */
    boolean isEqual(T object1, T object2);
}
