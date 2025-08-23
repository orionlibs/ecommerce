/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.core.ui.WidgetInstance;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides access to the current cockpit session lifecycle and attributes.
 */
public interface CockpitSessionService
{
    /**
     * Gets attribute for the given key
     *
     * @param string
     *           key from which to retrieve the attribute
     * @return the session attribute for the given key or null, if none existing.
     */
    Object getAttribute(String string);


    /**
     * Removes the session attribute for the given key, if existing.
     *
     * @param string
     *           key from which to remove the attribute
     */
    void removeAttribute(String string);


    /**
     * Sets the session attribute for the given key to the specified value.
     *
     * @param string
     *           key from which to set value
     * @param value
     *           to store
     */
    void setAttribute(String string, Object value);


    /**
     * Performs necessary steps to logout the current user.
     */
    void logout();


    /**
     * Gets widget instance map from the session
     *
     * @return map
     */
    default Map<String, WidgetInstance> getWidgetInstanceMap()
    {
        return new ConcurrentHashMap<>();
    }


    /**
     * Puts widget instance map to the session
     *
     * @param ret
     *           map
     */
    default void setWidgetInstanceMap(final Map<String, WidgetInstance> ret)
    {
    }
}
