/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.perspectivechooser.perspectiveresolver;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationTree;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.List;

/**
 * Simple implementation of the {@link DefaultPerspectiveResolver} that returns first
 * {@link com.hybris.backoffice.navigation.NavigationNode} which has no children from
 * {@link com.hybris.backoffice.navigation.NavigationTree}
 */
public class SequenceDefaultPerspectiveResolver implements DefaultPerspectiveResolver
{
    /**
     * @deprecated since 6.7 - use
     *             {@link SequenceDefaultPerspectiveResolver#resolveDefaultPerspective(NavigationTree, WidgetInstanceManager)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    @Override
    public NavigationNode resolveDefaultPerspective(final NavigationTree navigationTree)
    {
        return getFirstLeaf(navigationTree);
    }


    @Override
    public NavigationNode resolveDefaultPerspective(final NavigationTree navigationTree,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return getFirstLeaf(navigationTree);
    }


    private NavigationNode getFirstLeaf(final NavigationTree tree)
    {
        return getFirstLeafRecursive(tree.getRootNodes());
    }


    private NavigationNode getFirstLeafRecursive(final List<NavigationNode> nodes)
    {
        return nodes.stream().map(node -> {
            final List<NavigationNode> children = node.getChildren();
            if(children.isEmpty())
            {
                return node;
            }
            else
            {
                return getFirstLeafRecursive(children);
            }
        }).findFirst().orElse(null);
    }
}
