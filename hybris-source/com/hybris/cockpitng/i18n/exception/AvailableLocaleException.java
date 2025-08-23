/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.i18n.exception;

// TODO should be a checked exception as the client can recover from it
public class AvailableLocaleException extends RuntimeException
{
    private static final long serialVersionUID = -7463395357443179727L;


    public AvailableLocaleException()
    {
        super();
    }


    public AvailableLocaleException(final String message)
    {
        super(message);
    }


    public AvailableLocaleException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
