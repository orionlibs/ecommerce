/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.exceptions;

/**
 * Thrown if something went wrong with accessing objects.
 */
public class ObjectAccessException extends Exception
{
    private static final long serialVersionUID = -3729739845641686155L;


    /**
     *
     * @param msg - Full exception message
     * @param cause - root cause.
     */
    public ObjectAccessException(final String msg, final Throwable cause)
    {
        super(msg, cause);
    }
}
