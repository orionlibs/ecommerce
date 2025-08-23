/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.services.impl.expression;

/**
 * Exception thrown when a property value may not be provided, because current user is not allowed to read it.
 */
public class RestrictedAccessException extends IllegalArgumentException
{
    public RestrictedAccessException(final String message)
    {
        super(message);
    }
}