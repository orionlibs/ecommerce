/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util;

/**
 * Interface providing a reset method. Useful for caches or other classes that could be reset.
 */
public interface Resettable
{
    /**
     * Resets the something the implementation provides.
     */
    void reset();
}
