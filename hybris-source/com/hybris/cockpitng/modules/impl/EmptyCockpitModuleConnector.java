/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.impl;

import com.hybris.cockpitng.modules.server.ws.jaxb.CockpitModuleInfo;

/**
 * Dummy cockpit module connector.
 */
public class EmptyCockpitModuleConnector extends AbstractCockpitModuleConnector
{
    @Override
    public CockpitModuleInfo getModuleInfo(final String moduleUrl, final boolean cached)
    {
        return null;
    }
}
