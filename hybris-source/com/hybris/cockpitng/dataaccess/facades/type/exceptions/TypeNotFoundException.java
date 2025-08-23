/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.type.exceptions;

/**
 * Thrown, if a requested type can not be resolved.
 */
public class TypeNotFoundException extends Exception
{
    private static final long serialVersionUID = -3205079376861951510L;


    public TypeNotFoundException(final String typeCode)
    {
        super("Could not find type with code '" + typeCode + "'.");
    }


    public TypeNotFoundException(final String typeCode, final Throwable cause)
    {
        super("Could not find type with code '" + typeCode + "'.", cause);
    }
}
