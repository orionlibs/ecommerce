/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.exceptions;

import java.util.Collection;

/**
 * Thrown, if permission configuration doesn't allow object manipulation.
 */
public class ObjectSavePermissionException extends ObjectSavingException
{
    private static final long serialVersionUID = -8093900226509067984L;
    private final Collection<String> notPermittedProperties;


    public ObjectSavePermissionException(final String objectId, final Collection<String> notPermittedProperties)
    {
        super(objectId, null);
        this.notPermittedProperties = notPermittedProperties;
    }


    public Collection<String> getNotPermittedProperties()
    {
        return notPermittedProperties;
    }
}
