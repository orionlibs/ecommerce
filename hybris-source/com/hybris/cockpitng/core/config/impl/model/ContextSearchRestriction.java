/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

/**
 * An interface that provides information about which attributes may not change. There is a possibility to prevent
 * search mechanism from changing values of attributes for any reasons.
 * <P>
 * A restriction mechanism is used i.e. when obligatory merge mechanism is started - no attributes with higher priority
 * may be changed during search through obligatory merge.
 */
public interface ContextSearchRestriction
{
    /**
     * Checks whether specified attribute may be changed
     *
     * @param name
     *           name of attribute to check
     * @return <code>true</code> if value of attribute cannot be changed
     */
    boolean isAttributeRestricted(final String name);


    /**
     * Empty restriction - all attributes may be changed if needed
     */
    ContextSearchRestriction EMPTY = name -> false;
}
