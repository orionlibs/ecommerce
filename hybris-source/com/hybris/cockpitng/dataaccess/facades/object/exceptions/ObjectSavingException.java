/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.exceptions;

/**
 * Thrown, if something went wrong during saving of an object.
 */
public class ObjectSavingException extends ObjectAccessException
{
    private final String objectId;


    /**
     * Creates exception message using {@link #getMessageWithObjectId(String)}
     *
     * @param objectId
     *           is put into message and passed to super class constructor. Additionally it can be retrieved by calling
     *           {@link #getObjectId()}.
     * @param cause
     *           is re-passed to superclass constructor
     */
    public ObjectSavingException(final String objectId, final Throwable cause)
    {
        super(getMessageWithObjectId(objectId), cause);
        this.objectId = objectId;
    }


    /**
     * @param objectId
     *           can be retrieved by calling {@link #getObjectId()}
     * @param message
     *           is re-passed to superclass constructor
     * @param cause
     *           is re-passed to superclass constructor and cause objectId
     */
    public ObjectSavingException(final String objectId, final String message, final Throwable cause)
    {
        super(message, cause);
        this.objectId = objectId;
    }


    private static String getMessageWithObjectId(final String objectId)
    {
        return "Object " + objectId + " could not be saved";
    }


    public String getObjectId()
    {
        return objectId;
    }
}
