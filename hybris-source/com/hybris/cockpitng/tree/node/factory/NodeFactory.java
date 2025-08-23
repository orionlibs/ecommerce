/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.node.factory;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.cockpitng.config.explorertree.jaxb.ExplorerNode;
import com.hybris.cockpitng.core.context.CockpitContext;

/**
 * Factory of {@link ExplorerNode}.
 */
public interface NodeFactory<T extends ExplorerNode>
{
    /**
     * Creates node using given node configuration.
     */
    NavigationNode createNavigationNode(T nodeConfig);


    /**
     * Creates node using given node configuration and context
     *
     * @param nodeConfig node configuration
     * @param context node context
     * @return Newly created NavigationNode
     */
    default NavigationNode createNavigationNode(final T nodeConfig, final CockpitContext context)
    {
        return createNavigationNode(nodeConfig);
    }
}
