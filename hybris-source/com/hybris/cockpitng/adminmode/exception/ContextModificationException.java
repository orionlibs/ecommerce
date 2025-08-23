/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.adminmode.exception;

public class ContextModificationException extends RuntimeException
{
    private final String localizedMessage;


    public ContextModificationException(final String message, final String localizedMessage)
    {
        super("Added new context when it was forbidden: " + message);
        this.localizedMessage = localizedMessage;
    }


    @Override
    public String getLocalizedMessage()
    {
        return localizedMessage;
    }
}
