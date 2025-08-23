/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.navigation.impl;

import com.google.common.collect.Lists;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationTree;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class DefaultNavigationTree implements NavigationTree
{
    private static final long serialVersionUID = -5590777835258496605L;
    private List<NavigationNode> rootNodes;


    public DefaultNavigationTree()
    {
        this.rootNodes = Lists.newArrayList();
    }


    public DefaultNavigationTree(final NavigationNode rootNode)
    {
        this();
        this.rootNodes.add(rootNode);
    }


    public void addRootNode(final NavigationNode navNode)
    {
        if(navNode != null)
        {
            if(this.rootNodes == null)
            {
                this.rootNodes = new ArrayList<>();
            }
            this.rootNodes.add(navNode);
        }
    }


    @Override
    public List<NavigationNode> getRootNodes()
    {
        return this.rootNodes == null ? Collections.<NavigationNode>emptyList() : this.rootNodes;
    }


    public void removeRootNode(final NavigationNode navNode)
    {
        if(navNode != null && CollectionUtils.isNotEmpty(this.rootNodes) && this.rootNodes.contains(navNode))
        {
            this.rootNodes.remove(navNode);
        }
    }


    public void setRootNodes(final List<NavigationNode> rootNodes)
    {
        this.rootNodes = rootNodes;
    }
}
