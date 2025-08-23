/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.exceptions;

/**
 * Thrown, if an error occurred during the deletion of an object.
 */
public class ObjectDeletionException extends ObjectAccessException
{
    private static final long serialVersionUID = 5609694739522690297L;


    /**
     * Creates a new instance using given object id in a predefined message template.
     *
     * @param objectId - string message
     * @param cause root cause
     */
    public ObjectDeletionException(final String objectId, final Throwable cause)
    {
        super("Object '" + objectId + "' could not be deleted", cause);
    }


    /**
     * Creates a new instance using given object id in a predefined message template : "Object 'objectId' could not be deleted"
     *
     * @param objectId - ID of the object that could not be deleted.
     */
    public ObjectDeletionException(final String objectId)
    {
        this(objectId, null);
    }
}
