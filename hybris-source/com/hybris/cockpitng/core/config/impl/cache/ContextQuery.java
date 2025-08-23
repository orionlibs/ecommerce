/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.cache;

import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import java.util.Collection;

/**
 * Object that enables filtering available contexts in cache by their attributes.
 */
public interface ContextQuery
{
    /**
     * Extends query by adding filter for context configuration. New filter passes only those contexts that contain any
     * configuration.
     *
     * @return extended query
     */
    ContextQuery notEmpty();


    /**
     * Extends query by adding filter for specified attribute. New filter passes only those contexts that contains an
     * attribute with specified name and value is one of provided.
     *
     * @param name
     *           name of attribute to match
     * @param values
     *           collection of possible values
     * @return extended query
     */
    ContextQuery matchesAny(final String name, final Collection<String> values);


    /**
     * Executes query on cached contexts
     *
     * @return collection of contexts that matches current query or <code>null</code> if no contexts are available in
     *         cache
     */
    Collection<Context> execute();
}
