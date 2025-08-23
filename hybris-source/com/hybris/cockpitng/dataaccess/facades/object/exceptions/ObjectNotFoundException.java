/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.exceptions;

/**
 * Thrown, if a requested object could not be found.
 */
public class ObjectNotFoundException extends ObjectAccessException
{
    private static final long serialVersionUID = -1552471593654584661L;


    public ObjectNotFoundException(final String objectId, final Throwable cause)
    {
        super(String.format("Object '%s' not found%s", objectId, cause != null ? ": " + cause.getLocalizedMessage() : ""), cause);
    }


    public ObjectNotFoundException(final String objectId)
    {
        this(objectId, null);
    }
}
