/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util;

import java.util.Map;

/**
 * Provides methods to read cockpit configuration properties.
 */
public interface CockpitProperties
{
    /**
     * Returns property's value.
     *
     * @param key
     *           key of the property to return.
     * @return String value of the property stored under the key.
     */
    String getProperty(final String key);


    /**
     * Returns property's value as boolean. If the type of the value is known it is recommended to use this dedicated method
     * rather than parsing the value inline while caching may be enabled (implementation dependent).
     *
     * @param key
     *           key of the property to return.
     * @return boolean value stored under the given key, default to null if the value is not provided or cannot be
     *         interpreted.
     */
    boolean getBoolean(final String key);


    /**
     * Returns property's value as boolean. If the type of the value is known it is recommended to use this dedicated method
     * rather than parsing the value inline while caching may be enabled (implementation dependent).
     *
     * @param key
     *           key of the property to return.
     * @param defaultValue
     *           value to be returned if properties contains value for provided key
     * @return boolean value stored under the given key or {code}defaultValue{code} if the value is not provided or cannot
     *         be interpreted.
     */
    default boolean getBoolean(final String key, final boolean defaultValue)
    {
        final String property = getProperty(key);
        return property != null ? Boolean.valueOf(property) : defaultValue;
    }


    /**
     * Returns property's value as integer. If the type of the value is known it is recommended to use this dedicated method
     * rather than parsing the value inline while caching may be enabled (implementation dependent).
     *
     * @param key
     *           key of the property to return.
     * @return boolean value stored under the given key, default to null if the value is not provided or cannot be
     *         interpreted.
     */
    int getInteger(final String key);


    /**
     * Returns property's value as double. If the type of the value is known it is recommended to use this dedicated method
     * rather than parsing the value inline while caching may be enabled (implementation dependent).
     *
     * @param key
     *           key of the property to return.
     * @return boolean value stored under the given key, default to null if the value is not provided or cannot be
     *         interpreted.
     */
    double getDouble(final String key);


    /**
     * Returns all stored properties. This method can be resource-consuming and should be use only when user needs access to
     * all properties.
     *
     * @return the map with all properties.
     */
    Map<String, String> getProperties();
}
