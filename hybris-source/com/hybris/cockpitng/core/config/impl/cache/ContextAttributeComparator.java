/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.cache;

/**
 * An object able to compare two values of same attribute
 */
public interface ContextAttributeComparator
{
    /**
     * Checks whether provided value matches pattern
     *
     * @param attribute name of attribute which values are to be compared
     * @param pattern model value of attribute
     * @param value value to be checked
     * @return <code>true</code> if provided value matches provided pattern
     */
    boolean matches(final String attribute, final String pattern, final String value);
}
