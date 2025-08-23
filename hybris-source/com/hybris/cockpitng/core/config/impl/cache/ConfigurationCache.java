/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.cache;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchRequest;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Cache for {@link com.hybris.cockpitng.core.config.impl.jaxb.Config} and all its related objects
 */
public interface ConfigurationCache
{
    /**
     * Checks whether configuration is up to date
     *
     * @param origin
     *           last modification time of configuration
     * @return <code>true</code> if cache was valid and no changes where done
     */
    boolean isValid(final long origin);


    /**
     * Removes all values that has been cached
     */
    void clear();


    /**
     * Returns root configuration from cache
     *
     * @return cached configuration or <code>null</code> if not available
     */
    Config getRootConfiguration();


    /**
     * Adds root configuration to cache.
     * <P>
     * If any root configuration was already cached, then it is replaced
     *
     * @param config
     *           configuration to cache
     * @param origin
     *           origin time of configuration that is being cached
     */
    void cacheRootConfiguration(final Config config, final long origin);


    /**
     * Gets cached contexts for provided configuration
     *
     * @param config
     *           configuration which contexts are requested
     * @return contexts cached or <code>null</code> if unavailable
     */
    Collection<Context> getContexts(final Config config);


    /**
     * Sets contexts for specified configuration.
     * <P>
     * If any contexts for this configuration was already cached, then they are replaced
     *
     * @param config
     *           configuration which contexts are being cached
     * @param contexts
     *           contexts to cache
     * @param resolver
     *           an object able to retrieve all needed data from context
     */
    void cacheContexts(final Config config, final Collection<Context> contexts, final ConfigurationContextResolver resolver);


    /**
     * Removes all attributes for all contexts that has been cached
     *
     * @see #getAttributes(Context)
     */
    void clearAttributes();


    /**
     * Removes all attributes for specified context that has been cached
     *
     * @see #getAttributes(Context)
     */
    void clearAttributes(final Context context);


    /**
     * Gets cached attributes defined by provided context
     *
     * @param context
     *           context which attributes are requested
     * @return attributes &lt;name, value&gt; map or <code>null</code> if unavailable
     */
    Map<String, String> getAttributes(final Context context);


    /**
     * Prepares an object that is able to decide which queries should not be evaluated.
     *
     * @param request request that will be processed
     * @param constants attributes that will not change during the search
     * @return exclusions or <code>null</code> if all queries should be applied
     */
    ConfigurationSearchFilter prepareSearchFilter(final ContextSearchRequest request, final Set<String> constants,
                    final ContextAttributeComparator comparator);


    /**
     * Initialises search for context basing on their attributes
     *
     * @param config
     *           configuration which contexts are to be queried
     * @param comparator
     *           a object capable of comparing values of attributes
     * @return initialised query or <code>null</code> if configuration has been cached
     */
    ContextQuery createContextQuery(final Config config, final ContextAttributeComparator comparator);


    /**
     * Removes all configurations that has been cached.
     *
     * @see #getConfiguration(ConfigContext, Class)
     * @see #cacheConfiguration(ConfigContext, Class, Object)
     */
    void clearConfigurations();


    /**
     * Gets configuration from cache.
     *
     * @param <C>
     *           type of configuration
     * @param context
     *           context of requested configuration
     * @param configType
     *           expected configuration model class
     * @return configuration found or <code>null</code> if not available
     */
    <C> C getConfiguration(final ConfigContext context, final Class<C> configType);


    /**
     * Sets configuration in cache.
     * <P>
     * If any configurations bound to provided context was already cached, then it is replaced
     *
     * @param context
     *           context of requested configuration
     * @param config
     *           configuration to be cached
     */
    <C> void cacheConfiguration(final ConfigContext context, final Class<C> configType, final C config);
}
