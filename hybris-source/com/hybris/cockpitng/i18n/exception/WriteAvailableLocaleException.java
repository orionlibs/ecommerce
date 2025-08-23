/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.i18n.exception;

public class WriteAvailableLocaleException extends AvailableLocaleException
{
    private static final long serialVersionUID = 5674166190361478473L;


    public WriteAvailableLocaleException(final String message)
    {
        super(message);
    }


    public WriteAvailableLocaleException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
