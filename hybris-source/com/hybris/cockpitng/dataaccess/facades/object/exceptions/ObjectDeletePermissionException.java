/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.exceptions;

/**
 * Thrown, if permission configuration doesn't allow object deletion.
 */
public class ObjectDeletePermissionException extends ObjectDeletionException
{
    private static final long serialVersionUID = 2043491950445020760L;


    public ObjectDeletePermissionException(final String objectId)
    {
        super(objectId);
    }
}
