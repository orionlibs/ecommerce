/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

/**
 * Adapts cockpit configuration element according to given context.
 */
public interface CockpitConfigurationAdapter<CONFIG>
{
    /**
     * Returns the type of configuration element this adapter supports.
     *
     * @return type of configuration element this adapter supports
     */
    Class<CONFIG> getSupportedType();


    /**
     * Adapts given cockpit configuration element just after it is loaded and before it is merged to other subsequent
     * configurations.
     *
     * @param context
     *           configuration context
     * @param config
     *           configuration element
     * @return adapted cockpit configuration element
     * @throws CockpitConfigurationException
     *            if something went wrong
     */
    default CONFIG adaptBeforeMerge(final ConfigContext context, final CONFIG config) throws CockpitConfigurationException
    {
        return config;
    }


    /**
     * Adapts given cockpit configuration element after all subsequent configurations are loaded and merged.
     *
     * @param context
     *           configuration context
     * @param config
     *           configuration element
     * @return adapted cockpit configuration element
     * @throws CockpitConfigurationException
     *            if something went wrong
     */
    CONFIG adaptAfterLoad(ConfigContext context, CONFIG config) throws CockpitConfigurationException;


    /**
     * Adapts given cockpit configuration element before it is stored.
     *
     * @param context
     *           configuration context
     * @param config
     *           configuration element
     * @return adapted cockpit configuration element
     * @throws CockpitConfigurationException
     *            if something went wrong
     */
    CONFIG adaptBeforeStore(ConfigContext context, CONFIG config) throws CockpitConfigurationException;
}
