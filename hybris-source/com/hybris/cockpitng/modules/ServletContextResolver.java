/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules;

import javax.servlet.ServletContext;

/**
 * Resolves a {@link ServletContext} for a given environment, e.g. current Thread.
 */
public interface ServletContextResolver
{
    /**
     * Return a {@link ServletContext} if it can be determined or null otherwise.
     */
    ServletContext getServletContext();
}
