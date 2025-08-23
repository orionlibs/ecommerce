/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin.strategy.socket;

import com.hybris.cockpitng.core.WidgetSocket;

/**
 * Strategy used to determine if two sockets match. Usually it means that the sockets have matching types and direction
 * (in/out) plus some additional, strategy specific criteria.
 */
public interface WidgetSocketMatchStrategy
{
    /**
     * Identifier of the strategy. It should be unique.
     *
     * @return identifier of the strategy
     */
    String getStrategyCode();


    /**
     * Method checks if two sockets match, ie. may be connected.
     *
     * @param socketA a socket
     * @param socketB a socket
     * @return true if the two sockets match
     */
    boolean matches(WidgetSocket socketA, WidgetSocket socketB);
}
