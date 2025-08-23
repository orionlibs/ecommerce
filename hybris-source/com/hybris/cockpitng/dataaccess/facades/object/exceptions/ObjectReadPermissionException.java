/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.exceptions;

/**
 * Thrown, if permission configuration doesn't allow object reading.
 */
public class ObjectReadPermissionException extends ObjectNotFoundException
{
    private static final long serialVersionUID = 3619810454672075548L;


    public ObjectReadPermissionException(final String objectId)
    {
        super(objectId);
    }
}
