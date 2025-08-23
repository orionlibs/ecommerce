/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

/**
 * Interface of an object that needs and is able to do some cleanup before application view is destroyed (i.e. before
 * application refresh).
 */
public interface Cleanable
{
    /**
     * Cleans all references that will be no longer valid after component is detached from its parent.
     * <P>
     * Notice: if an object implements {@link Initializable} also, then it is possible that {@link #cleanup()} will be called
     * several times without calling {@link Initializable#initialize()}.
     */
    void cleanup();
}
