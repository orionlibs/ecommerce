/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object;

/**
 * Strategy for {@link ObjectFacade}
 */
public interface ObjectFacadeStrategy extends ObjectCRUDHandler
{
    /**
     * Returns true, if this strategy implementation can handle the given object, otherwise false.
     */
    <T> boolean canHandle(T object);
}
