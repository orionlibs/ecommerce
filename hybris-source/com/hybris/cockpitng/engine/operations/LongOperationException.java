/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.operations;

/**
 * Defines exception thrown during long operation.
 */
public class LongOperationException extends RuntimeException
{
    public LongOperationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }


    public LongOperationException(final Throwable cause)
    {
        super(cause);
    }
}
