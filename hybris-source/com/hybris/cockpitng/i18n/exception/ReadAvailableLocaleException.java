/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.i18n.exception;

public class ReadAvailableLocaleException extends AvailableLocaleException
{
    private static final long serialVersionUID = 7130650988651255265L;


    public ReadAvailableLocaleException(final String message)
    {
        super(message);
    }


    public ReadAvailableLocaleException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
