/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

import java.util.Collection;

/**
 * Configuration context search tree.
 * <p>
 * Searching of contexts is performed by traversing through context tree in regards to <code>merge-by</code> attributes,
 * obligatory merge attributes and/or appropriate
 * {@link com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy}. Each step down the tree means that any
 * contexts found are a little bit less relevant to actual request. It is represented by search level and results in
 * lower priority during merge process - a context with lower priority (found on lower level) is merged into a context
 * with higher priority (found on higher level).
 */
public class ContextSearchTree extends DefaultContextSearchNode
{
    /**
     * Relevance zones for this search
     *
     * @param relevanceZones
     *           relevance zones in order from most important to least important
     */
    public ContextSearchTree(final Collection<String> relevanceZones)
    {
        super(null, ContextSearchNodeRelevance.mostRelevant(relevanceZones));
    }


    @Override
    protected void setParentImmediately(final ContextSearchNode parent)
    {
        throw new UnsupportedOperationException("Unable to set parent to tree");
    }
}
