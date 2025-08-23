/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.handler.login;

import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import java.util.List;

/**
 * Service that allows reading and writing login specific information.
 * Note: This service is session scope service
 *
 */
public interface LoginInformationHandler
{
    /**
     * Gets current login screen configuration.
     *
     * @return current login screen configuration i.e. list of {@link LoginInformationConfigData} instances.
     */
    List<LoginInformationConfigData> getConfiguration();


    /**
     * Returns container contains a login specific information
     *
     * @return Instance of {@link TypedSettingsMap} contains information filled by user on a login screen.
     */
    TypedSettingsMap getLoginInformation();


    /**
     * Return a login information for a given key.
     *
     * @param key login information key
     * @return a login information value for a given key
     */
    Object getLoginInformation(final String key);


    /**
     * Stores given value at given key.
     *
     * @param key information key
     * @param value information value
     */
    void putLoginInformation(final String key, final Object value);
}
