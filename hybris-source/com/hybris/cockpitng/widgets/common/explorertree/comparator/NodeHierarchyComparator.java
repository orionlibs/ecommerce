/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree.comparator;

import com.hybris.backoffice.navigation.NavigationNode;
import java.util.Comparator;
import java.util.Objects;

public class NodeHierarchyComparator implements Comparator<NavigationNode>
{
    @Override
    public int compare(final NavigationNode n1, final NavigationNode n2)
    {
        if(Objects.equals(n1, n2))
        {
            return 0;
        }
        if(isParent(n2, n1))
        {
            return -1;
        }
        if(isParent(n1, n2))
        {
            return 1;
        }
        return 0;
    }


    private boolean isParent(final NavigationNode node, final NavigationNode parent)
    {
        if(node.getParent() != null)
        {
            if(Objects.equals(parent, node.getParent()))
            {
                return true;
            }
            return isParent(node.getParent(), parent);
        }
        return false;
    }
}
