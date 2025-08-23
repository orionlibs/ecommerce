/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.util;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationNodeDecoratorUtils;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.zul.TreeModel;

/**
 * Optimizes traversing in trees which store only {@link NavigationNode} nodes. To use it you should override
 * {@link TreeModel#getPath(Object)} and execute {@link #getPath(NavigationNode)}.
 */
public class NavigationTreeModelGetPathOptimizer
{
    private final Function<NavigationNode, List<NavigationNode>> childrenProvider;


    public NavigationTreeModelGetPathOptimizer()
    {
        this(node -> node.getChildren());
    }


    public NavigationTreeModelGetPathOptimizer(final Function<NavigationNode, List<NavigationNode>> childrenProvider)
    {
        this.childrenProvider = childrenProvider;
    }


    public int[] getPath(final NavigationNode child)
    {
        final Deque<Integer> indexes = new ArrayDeque<>();
        NavigationNode current = child;
        while(current != null)
        {
            final NavigationNode parent = current.getParent();
            if(parent != null)
            {
                final int indexOfNode = indexOf(current, childrenProvider.apply(parent));
                if(indexOfNode >= 0)
                {
                    indexes.push(Integer.valueOf(indexOfNode));
                }
                else
                {
                    return new int[0];
                }
            }
            current = parent;
        }
        return indexes.stream().mapToInt(Integer::intValue).toArray();
    }


    protected int indexOf(final NavigationNode node, final List<NavigationNode> nodes)
    {
        if(CollectionUtils.isEmpty(nodes))
        {
            return -1;
        }
        final NavigationNode nodeUnwrapped = NavigationNodeDecoratorUtils.unwrap(node);
        for(int index = 0; index < nodes.size(); ++index)
        {
            final NavigationNode item = nodes.get(index);
            final NavigationNode itemUnwrapped = NavigationNodeDecoratorUtils.unwrap(item);
            if(Objects.equals(nodeUnwrapped, itemUnwrapped))
            {
                return index;
            }
        }
        return -1;
    }
}
