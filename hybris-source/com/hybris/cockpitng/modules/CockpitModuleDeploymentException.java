/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules;

import com.hybris.cockpitng.core.CockpitApplicationInitializationException;

/**
 * Signals that an error occured when trying to deploy a remote cockpit module.
 */
public class CockpitModuleDeploymentException extends CockpitApplicationInitializationException
{
    public CockpitModuleDeploymentException()
    {
    }


    public CockpitModuleDeploymentException(final String message)
    {
        super(message);
    }


    public CockpitModuleDeploymentException(final String message, final Throwable cause)
    {
        super(message, cause);
    }


    public CockpitModuleDeploymentException(final Throwable cause)
    {
        super(cause);
    }


    public CockpitModuleDeploymentException(final String message, final Throwable cause, final boolean enableSuppression,
                    final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
