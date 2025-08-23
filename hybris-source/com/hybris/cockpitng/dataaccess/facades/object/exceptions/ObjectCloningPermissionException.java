/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.exceptions;

/**
 * Thrown, if permission configuration doesn't allow object cloning.
 */
public class ObjectCloningPermissionException extends ObjectCloningException
{
    private static final long serialVersionUID = 4066414653266362136L;


    public ObjectCloningPermissionException(final String objectId)
    {
        super(objectId, null);
    }
}
