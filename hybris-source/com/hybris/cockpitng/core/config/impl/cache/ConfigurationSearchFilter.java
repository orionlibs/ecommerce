/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.cache;

/**
 * Interface that filters possible configuration searches.
 * <P>
 * It is possible that in some situations there is no need to execute a particular search, as it is already known as
 * empty. In this case {@link ConfigurationSearchFilter} may filter such search out.
 */
public interface ConfigurationSearchFilter
{
    /**
     * Checks whether there is a need of executing particular search.
     *
     * @param name name of attribute that is about to change
     * @param value new value of attribute
     * @return <code>true</code> if this search may be omitted, <code>false</code> otherwise
     */
    boolean filter(final String name, final String value);
}
