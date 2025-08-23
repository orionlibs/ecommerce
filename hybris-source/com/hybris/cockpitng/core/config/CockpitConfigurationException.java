/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

/**
 * Represents an error occured in the cockpit configuration service.
 */
public class CockpitConfigurationException extends Exception
{
    private static final long serialVersionUID = 4842801690636237308L;


    public CockpitConfigurationException()
    {
        super();
    }


    public CockpitConfigurationException(final String message)
    {
        super(message);
    }


    public CockpitConfigurationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }


    public CockpitConfigurationException(final Throwable cause)
    {
        super(cause);
    }
}
