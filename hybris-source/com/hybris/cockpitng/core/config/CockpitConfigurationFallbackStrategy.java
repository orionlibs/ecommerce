/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

/**
 * Strategy for creating fallback configurations on the fly.
 */
public interface CockpitConfigurationFallbackStrategy<CONFIG>
{
    /**
     * Generates and loads a fallback configuration for the given context and configuration type.
     *
     * @param context configuration context
     * @param configurationType type of configuration to be loaded
     * @return created fallback configuration or <p>null</p> if the strategy can not create a fallback configuration
     *         for the given context and type
     */
    CONFIG loadFallbackConfiguration(ConfigContext context, Class<CONFIG> configurationType);
}
