/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

/**
 * Interface of an object that needs and is able to do some initialize before application view is built.
 */
public interface Initializable
{
    /**
     * Initializes all references that may be needed during its lifetime. Method is called right before a component is
     * attached and displayed.
     * <P>
     * Notice: if an object implements {@link Cleanable} also, then it is possible that {@link #initialize()} will be called
     * several times without calling {@link Cleanable#cleanup()}.
     */
    void initialize();
}
