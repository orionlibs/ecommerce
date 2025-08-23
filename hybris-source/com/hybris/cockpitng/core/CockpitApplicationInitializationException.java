/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

/**
 * Signals that an error occurred when initializing cockpit application
 */
public class CockpitApplicationInitializationException extends CockpitApplicationException
{
    public CockpitApplicationInitializationException()
    {
    }


    public CockpitApplicationInitializationException(final String message)
    {
        super(message);
    }


    public CockpitApplicationInitializationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }


    public CockpitApplicationInitializationException(final Throwable cause)
    {
        super(cause);
    }


    public CockpitApplicationInitializationException(final String message, final Throwable cause, final boolean enableSuppression,
                    final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
