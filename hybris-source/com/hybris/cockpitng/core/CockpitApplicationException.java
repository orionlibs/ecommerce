/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

/**
 * Signals an error related to core functionality of CockpitNG application
 */
public class CockpitApplicationException extends Exception
{
    public CockpitApplicationException()
    {
    }


    public CockpitApplicationException(final String message)
    {
        super(message);
    }


    public CockpitApplicationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }


    public CockpitApplicationException(final Throwable cause)
    {
        super(cause);
    }


    public CockpitApplicationException(final String message, final Throwable cause, final boolean enableSuppression,
                    final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
