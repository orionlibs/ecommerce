/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

/**
 * Represents a relevance level of single tree node.
 */
public interface NodeRelevance extends Comparable, Cloneable
{
    /**
     * Creates a copy of this relevance level
     *
     * @deprecated since 6.7
     * @return copy
     */
    @Deprecated(since = "6.7", forRemoval = true)
    NodeRelevance clone();
}
