/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

public class CockpitConfigurationUnavailable extends IllegalStateException
{
    public CockpitConfigurationUnavailable()
    {
        super();
    }


    public CockpitConfigurationUnavailable(final String s)
    {
        super(s);
    }


    public CockpitConfigurationUnavailable(final String message, final Throwable cause)
    {
        super(message, cause);
    }


    public CockpitConfigurationUnavailable(final Throwable cause)
    {
        super(cause);
    }
}
