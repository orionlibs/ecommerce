/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree.data;

import com.hybris.backoffice.navigation.NavigationNode;
import java.util.List;

/**
 * Data class used for nodes which number of children exceeded partitionThreshold. Such children's nodes are not
 * displayed directly as a children but the nodes are wrapped into additional nodes of range, for example '1 ... 100',
 * '101 ... 200'
 */
public class PartitionNodeData implements ActionAwareNode
{
    private final NavigationNode parent;
    private final List<NavigationNode> children;


    public PartitionNodeData(final NavigationNode parent, final List<NavigationNode> children)
    {
        this.parent = parent;
        this.children = children;
    }


    public List<NavigationNode> getChildren()
    {
        return children;
    }


    public NavigationNode getParent()
    {
        return parent;
    }
}
