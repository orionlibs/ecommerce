/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

/**
 * Represents an error occured in the cockpit configuration service, indicating that a requested
 * configuration context is not existing.
 */
public class CockpitConfigurationNotFoundException extends CockpitConfigurationException
{
    private static final long serialVersionUID = -936167451207386259L;


    public CockpitConfigurationNotFoundException()
    {
        super();
    }


    public CockpitConfigurationNotFoundException(final String msg)
    {
        super(msg);
    }


    public CockpitConfigurationNotFoundException(final String msg, final Throwable cause)
    {
        super(msg, cause);
    }


    public CockpitConfigurationNotFoundException(final Throwable cause)
    {
        super(cause);
    }
}
