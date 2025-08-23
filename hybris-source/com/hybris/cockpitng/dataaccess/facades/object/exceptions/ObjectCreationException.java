/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.exceptions;

/**
 * Thrown, if an error occurred during the creation of an object.
 */
public class ObjectCreationException extends ObjectAccessException
{
    private static final long serialVersionUID = 7227857314716111303L;
    private final String typeId;


    /**
     * Creates a new instance using given type id in a predefined message template.
     *
     * @param typeId id of object
     * @param cause root cause
     */
    public ObjectCreationException(final String typeId, final Throwable cause)
    {
        super(String.format("Object of type '%s' could not be created%s", typeId,
                        cause != null ? ": " + cause.getLocalizedMessage() : ""), cause);
        this.typeId = typeId;
    }


    /**
     * Creates a new instance using given type id in a predefined message template.
     *
     * @param typeId id of object
     */
    public ObjectCreationException(final String typeId)
    {
        this(typeId, null);
    }


    public String getTypeId()
    {
        return typeId;
    }
}
