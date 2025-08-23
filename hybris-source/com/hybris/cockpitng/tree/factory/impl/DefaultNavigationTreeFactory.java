/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.factory.impl;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationNodeDecorator;
import com.hybris.backoffice.navigation.NavigationTree;
import com.hybris.backoffice.navigation.impl.DefaultNavigationTree;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.cockpitng.config.explorertree.jaxb.DynamicNode;
import com.hybris.cockpitng.config.explorertree.jaxb.ExplorerNode;
import com.hybris.cockpitng.config.explorertree.jaxb.ExplorerTree;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.tree.factory.NavigationTreeFactory;
import com.hybris.cockpitng.tree.node.factory.NodeFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.BooleanUtils;

public class DefaultNavigationTreeFactory implements NavigationTreeFactory
{
    private Map<Class<? extends ExplorerNode>, NodeFactory> nodeFactories;


    @Override
    public NavigationTree createNavigationTree(final ExplorerTree explorerTreeConfig)
    {
        return createNavigationTree(explorerTreeConfig, new DefaultCockpitContext());
    }


    @Override
    public NavigationTree createNavigationTree(final ExplorerTree explorerTreeConfig, final CockpitContext context)
    {
        final DefaultNavigationTree ret = new DefaultNavigationTree();
        final SimpleNode rootNode = new SimpleNode(ROOT_NODE_ID);
        rootNode.setContext(context);
        rootNode.setNameLocKey(explorerTreeConfig.getTitle());
        populateChildren(explorerTreeConfig, rootNode, context);
        ret.setRootNodes(Collections.singletonList(rootNode));
        return ret;
    }


    @Override
    public void refreshRootNode(final ExplorerTree explorerTreeConfig, final NavigationNode rootNode, final CockpitContext context)
    {
        populateChildren(explorerTreeConfig, rootNode, context);
    }


    protected void populateChildren(final ExplorerTree explorerTreeConfig, final NavigationNode rootNode,
                    final CockpitContext context)
    {
        final List<NavigationNode> navigationNodes = new ArrayList<>();
        for(final ExplorerNode child : explorerTreeConfig.getNavigationNodeOrTypeNodeOrDynamicNode())
        {
            final NavigationNode parent = this.createNodeRecursively(child, context);
            if(parent != null)
            {
                if(child instanceof DynamicNode && BooleanUtils.isTrue(((DynamicNode)child).isHideRootNode()))
                {
                    parent.setParent(rootNode);
                    navigationNodes.addAll(parent.getChildren());
                }
                else
                {
                    navigationNodes.add(parent);
                }
            }
        }
        rootNode.setChildren(navigationNodes);
    }


    protected <T extends ExplorerNode> NavigationNode createNodeRecursively(final T config, final CockpitContext context)
    {
        final NodeFactory<ExplorerNode> nodeFactory = this.nodeFactories.get(config.getClass());
        final NavigationNode node = new NavigationNodeDecorator(nodeFactory.createNavigationNode(config, context));
        final List<NavigationNode> subNodes = new ArrayList<>();
        for(final ExplorerNode child : config.getNavigationNodeOrTypeNodeOrDynamicNode())
        {
            final NavigationNode childNode = this.createNodeRecursively(child, context);
            if(childNode != null)
            {
                subNodes.add(childNode);
            }
        }
        node.setChildren(subNodes);
        return node;
    }


    public void setNodeFactories(final Map<Class<? extends ExplorerNode>, NodeFactory> nodeFactories)
    {
        this.nodeFactories = nodeFactories;
    }
}
