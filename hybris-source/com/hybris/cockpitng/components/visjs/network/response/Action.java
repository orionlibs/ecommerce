/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.response;

/**
 * Represents possible actions while interacting with network chart
 */
public enum Action
{
    /**
     * Adds nodes/edges to existing network chart. If networkchart already contains element with the same id, an
     * exception in browser will be thrown. Therefore consider using {@link Action#UPDATE} instead
     */
    ADD,
    /**
     * Updated nodes/edges in existing network chart. If networkchart doesn't contain given elements, vis.js will create
     * them
     */
    UPDATE,
    /**
     * Removes nodes/edges from existing network chart
     */
    REMOVE
}
