/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.clone;

import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCloningException;
import org.springframework.core.Ordered;

/**
 * Strategy for cloning objects
 */
public interface CloneStrategy extends Ordered
{
    /**
     * Returns true, if this strategy implementation can handle the given object, otherwise false.
     */
    <T> boolean canHandle(T object);


    /**
     * Clone given object
     *
     * @param objectToClone
     *           object to be cloned
     * @return cloned object
     */
    <T> T clone(T objectToClone) throws ObjectCloningException;
}
