/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.node;

import com.hybris.backoffice.navigation.NavigationNode;
import java.util.List;

/**
 * Interface for populators of dynamic nodes used in navigation trees
 */
public interface DynamicNodePopulator
{
    /**
     * Get the children of current node
     *
     * @param node current node; on first (root) dynamic level it has to be {@link DynamicNode}
     * @return children of current node
     */
    List<NavigationNode> getChildren(NavigationNode node);
}
