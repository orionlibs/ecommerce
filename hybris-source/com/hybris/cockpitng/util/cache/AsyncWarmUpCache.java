/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A cache performing asynchronous warming up.
 */
public interface AsyncWarmUpCache<K, V>
{
    /**
     * Triggers asynchronous cache warm up.
     *
     * @param keys
     *           iterator over keys to be processed
     * @param values
     *           value provider
     */
    void warmUp(final Iterator<Collection<K>> keys, final Function<K, V> values);


    /**
     * Checks whether a value for specified key is cached.
     *
     * @param key
     *           value key
     * @return <code>true</code> if value is cached
     */
    boolean has(final K key);


    /**
     * Gets cached value for specified key.
     *
     * @param key
     *           value key
     * @return value for key
     */
    V get(final K key);


    /**
     * Gets cached value for specified key. If cache has no value for specified key, default value provider is asked for it.
     *
     * @param key
     *           value key
     * @param defaultValue
     *           a supplier for a result in case value is not cached
     * @return value for key
     */
    V get(final K key, final Supplier<V> defaultValue);


    /**
     * Removes a value cached for specified key.
     *
     * @param key
     *           value key
     */
    void revoke(final K key);


    /**
     * Clears whole cache.
     */
    void clear();
}
