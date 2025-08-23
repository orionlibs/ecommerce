/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.factory;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationTree;
import com.hybris.cockpitng.config.explorertree.jaxb.ExplorerTree;
import com.hybris.cockpitng.core.context.CockpitContext;

/**
 * Factory class for navigation tree models.
 */
public interface NavigationTreeFactory
{
    /**
     * Root nodes should have this constant assigned as their Id. It is crucial for refreshing of the root nodes while
     * this constant is used for node lookup in the Explorer Tree.
     */
    String ROOT_NODE_ID = "_y_et_root_";


    /**
     * Factory method creating tree model accordingly to the given configuration.
     *
     * @param explorerTreeConfig
     *           - tree configuration
     * @return tree model
     */
    NavigationTree createNavigationTree(ExplorerTree explorerTreeConfig);


    /**
     * Factory method creating tree model accordingly to the given configuration and context
     *
     * @param explorerTreeConfig
     *           tree configuration
     * @param context
     *           tree context
     * @return tree model
     */
    default NavigationTree createNavigationTree(final ExplorerTree explorerTreeConfig, final CockpitContext context)
    {
        return createNavigationTree(explorerTreeConfig);
    }


    /**
     * @param explorerTreeConfig
     *           tree configuration
     * @param rootNode
     *           navigation node which represents root node
     * @param context
     *           tree context
     */
    default void refreshRootNode(final ExplorerTree explorerTreeConfig, final NavigationNode rootNode,
                    final CockpitContext context)
    {
    }
}
