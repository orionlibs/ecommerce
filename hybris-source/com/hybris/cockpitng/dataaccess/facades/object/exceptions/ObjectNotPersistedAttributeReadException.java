/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.exceptions;

/**
 * Thrown if an attribute cannot be read from not persisted object.
 */
public class ObjectNotPersistedAttributeReadException extends RuntimeException
{
    public ObjectNotPersistedAttributeReadException(final String objectType)
    {
        super(objectType);
    }
}
