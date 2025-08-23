/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.navigation;

import com.hybris.backoffice.actionbar.ActionsTree;
import java.util.List;

/**
 * Defines a tree structure consisting of {@link NavigationNode}s.
 */
public interface NavigationTree extends ActionsTree<NavigationNode>
{
    /**
     * Returns the list of root nodes of the tree.
     *
     * @return list of root nodes of the tree
     */
    @Override
    List<NavigationNode> getRootNodes();
}
