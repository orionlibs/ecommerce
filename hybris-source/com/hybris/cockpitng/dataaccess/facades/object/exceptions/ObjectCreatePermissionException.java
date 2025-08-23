/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.exceptions;

/**
 * Thrown, if permission configuration doesn't allow object creation.
 */
public class ObjectCreatePermissionException extends ObjectCreationException
{
    private static final long serialVersionUID = 4066414653266362136L;


    public ObjectCreatePermissionException(final String objectId)
    {
        super(objectId);
    }
}
