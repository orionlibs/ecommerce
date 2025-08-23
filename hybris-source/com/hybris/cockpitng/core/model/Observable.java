/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Object that contains some values. It is possible to observe changes of those values
 */
public interface Observable
{
    /**
     * Informs observers that some values were changed.
     */
    void changed();


    /**
     * Informs observers that some value has changed.
     *
     * @param key
     *           name of value that has changed
     */
    default void changed(final String key)
    {
        changed();
    }


    /**
     * Registers a {@link ValueObserver} for changes of values. The observer will be notified each time any value is
     * changed.
     * <P>
     * Each model observer may be registered only once (next registrations will be ignored). Model observers are by
     * default compared by their equals(Object) method. To change this behaviour, observer may implement
     * {@link Identifiable} interface to introduce observer's identity.
     *
     * @param observer
     *           the observer to be notified about changes
     * @see #addObserver(String, ValueObserver)
     */
    default void addObserver(final ValueObserver observer)
    {
        addObserver(StringUtils.EMPTY, observer);
    }


    /**
     * Registers a {@link ValueObserver} for changes of values. The observer will be notified each time a value related
     * to the given <code>key</code> is changed.
     * <P>
     * i.e. if the observer is registered for <code>product.media.code</code> it will be notified if value on any level
     * of the key (<code>product</code>, <code>product.media</code> or <code>product.media.code</code>) was changed. Also
     * for <code>product.media</code> it will be notified, if <code>product.media.code</code> has changed.
     * <P>
     * Each model observer may be registered only once (next registrations will be ignored). Model observers are by
     * default compared by their equals(Object) method. To change this behaviour, observer may implement
     * {@link Identifiable} interface to introduce observer's identity.
     *
     * @param key
     *           attribute for changes of which the observer is interested
     * @param modelObserver
     *           the observer to be notified about changes
     * @see #addObserver(ValueObserver)
     */
    void addObserver(String key, ValueObserver modelObserver);


    /**
     * Removes a previously registered {@link ValueObserver}.
     *
     * @param key
     *           attribute for changes of which the observer was interested
     * @param modelObserver
     *           the observer to be removed
     * @see #addObserver(String, ValueObserver)
     * @see #removeObserver(ValueObserver)
     */
    void removeObserver(String key, ValueObserver modelObserver);


    /**
     * Removes a previously registered {@link ValueObserver} for any key.
     *
     * @param modelObserver
     *           the observer to be removed
     * @see #addObserver(ValueObserver)
     * @see #removeObserver(String, ValueObserver)
     */
    void removeObserver(ValueObserver modelObserver);
}
