/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.events;

import java.util.Collection;
import java.util.Collections;

/**
 * Interface representing a cockpit event.
 */
public interface CockpitEvent
{
    /**
     * The desktop event scope is the narrowest available scope. Its events are only defined for a specific desktop.
     * (Scope: 4)
     * e.g. propagation of an event to a single browser tab or just a part of a tab
     */
    String DESKTOP = "desktop";
    /**
     * The user event scope which events are propagated to all sessions of a user. (Scope: 2)
     * e.g. propagation of an event to different machines w/ different tabs of a specific user
     */
    String USER = "user";
    /**
     * The session event scope which events are propagated to the current user on all active desktops within the same
     * session. (Scope: 3)
     * e.g. propagation of an event to different tabs within the same browser
     */
    String SESSION = "session";
    /**
     * The application event scope is the most global event scope which events are propagated to all other scopes.
     * (Scope: 1)
     */
    String APPLICATION = "application";


    /**
     * @return The name of the event.
     */
    String getName();


    /**
     * @return The data attached to the event. Can be null.
     */
    Object getData();


    /**
     * @return The event source, i.e. the object which has fired the event. Can be null.
     */
    Object getSource();


    /**
     * If the object is an instance of Collection then the collection is returned as unmodifiable,
     * when the object is not a Collection then a unmodifiable Collection with the single object is returned,
     * when object is null then empty unmodifiable Collection is returned.
     */
    default java.util.Collection<Object> getDataAsCollection()
    {
        final Object object = getData();
        final Collection collection;
        if(object instanceof Collection)
        {
            collection = Collections.unmodifiableCollection((Collection<?>)object);
        }
        else if(object != null)
        {
            collection = Collections.singletonList(object);
        }
        else
        {
            collection = Collections.emptyList();
        }
        return collection;
    }
}
