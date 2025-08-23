/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.exception;

public class WidgetTestException extends RuntimeException
{
    private static final long serialVersionUID = 6480341989155502701L;


    public WidgetTestException()
    {
        super();
    }


    public WidgetTestException(final String message)
    {
        super(message);
    }


    public WidgetTestException(final Throwable cause)
    {
        super(cause);
    }


    public WidgetTestException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
