/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.context;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface, which represents general Context of the Cockpit and allows to access its parameters
 */
public interface CockpitContext extends Serializable
{
    /**
     * Returns map of parameters
     *
     * @return parameters
     */
    Map<String, Object> getParameters();


    /**
     * Sets map of parameters
     *
     * @param parameters
     */
    void setParameters(final Map<String, Object> parameters);


    /**
     * Returns a boolean representation of the object stored under the given key or default value if the object is null.
     *
     * @param key
     *           parameter key.
     * @param defaultValue
     *           default value if the key is not found in the context or its value is null.
     * @return the boolean representation of the object or the default value.
     */
    default boolean getParameterAsBoolean(final String key, final boolean defaultValue)
    {
        final Object parameter = getParameter(key);
        if(parameter instanceof Boolean)
        {
            return ((Boolean)parameter).booleanValue();
        }
        if(parameter instanceof String)
        {
            return Boolean.parseBoolean((String)parameter);
        }
        return defaultValue;
    }


    /**
     * Returns a int representation of the object stored under the given key or default value if the object is null.
     *
     * @param key
     *           parameter key.
     * @param defaultValue
     *           default value if the key is not found in the context or its value is null.
     * @return the int representation of the object or the default value.
     */
    default int getParameterAsInt(final String key, final int defaultValue)
    {
        final Object value = getParameter(key);
        if(value instanceof Integer)
        {
            return ((Integer)value).intValue();
        }
        if(value instanceof String)
        {
            try
            {
                return Integer.parseInt((String)value);
            }
            catch(final NumberFormatException nfe)
            {
                final Logger logger = LoggerFactory.getLogger(CockpitContext.class);
                logger.warn("Couldn't parse integer from key: '{}' and value: '{}' ", key, value);
                return defaultValue;
            }
        }
        return defaultValue;
    }


    /**
     * Sets single parameter
     *
     * @param key
     *           parameter key
     * @param value
     *           parameter value
     */
    void setParameter(final String key, final Object value);


    /**
     * Gets single parameter
     *
     * @param key
     *           parameter key
     * @return Object stored under specified key
     */
    Object getParameter(final String key);


    /**
     * Removes single parameter with specified key
     *
     * @param key
     *           parameter key
     */
    void removeParameter(final String key);


    /**
     * Gets keys of the parameters
     *
     * @return Set containing keys of the parameters
     */
    Set<String> getParameterKeys();


    /**
     * Checks if given parameter exists
     *
     * @param key
     *           parameter key
     * @return Boolean, which is equal to true if parameter exists and is equal to false if not
     */
    boolean containsParameter(final String key);


    /**
     * Clears all parameters
     */
    void clearParameters();


    /**
     * Adds all parameters stored in given context
     *
     * @param context
     *           source
     */
    default void addAllParameters(final CockpitContext context)
    {
        // NOOP
    }
}
