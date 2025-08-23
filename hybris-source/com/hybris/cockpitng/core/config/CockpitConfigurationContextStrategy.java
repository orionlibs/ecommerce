/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * A strategy defining value matching and inheritance for a configuration context. For example: a user role based
 * strategy could implement user name matching and user/group inheritance lookup for a context holding user name.
 */
public interface CockpitConfigurationContextStrategy
{
    List<String> EMPTY_PARENT_CONTEXT = Collections.unmodifiableList(Collections.singletonList(StringUtils.EMPTY));


    /**
     * Gets parent values for provided one.
     *
     * @param context
     *           value which parent is requested
     * @return list of parent values
     */
    List<String> getParentContexts(String context);


    /**
     * Checks if value of strategy's attribute should be reset up to original value after match.
     * <p>
     * Search algorithm after a match looks for parent contexts. There is a possibility, that some of attributes should be
     * reset to original value before looking for parents (i.e. module).
     * </p>
     *
     * @return <code>true</code> if value should be reset
     */
    default boolean isResettable()
    {
        return false;
    }


    /**
     * Checks if provided value matches the one defined in configuration context.
     *
     * @param contextValue
     *           value defined in configuration context
     * @param value
     *           value to be checked
     * @return <code>true</code> if provided value matches the one defined in configuration context
     */
    boolean valueMatches(String contextValue, String value);


    /**
     * Wraps provided ConfigContext, adds some attributes to make the configuration context more specific and returns it.
     *
     * @param configContext
     *           source
     * @return wrapped configContext
     */
    default ConfigContext getConfigurationCacheKey(final ConfigContext configContext)
    {
        return configContext;
    }
}
