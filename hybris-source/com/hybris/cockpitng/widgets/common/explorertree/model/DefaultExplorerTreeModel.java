/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree.model;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationNodeDecoratorUtils;
import com.hybris.backoffice.navigation.NavigationTree;
import com.hybris.cockpitng.common.model.ObjectWithComponentContext;
import com.hybris.cockpitng.tree.util.NavigationTreeModelGetPathOptimizer;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.SetUtils;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.ext.Sortable;

/**
 * Default tree model for the explorer tree widget.
 */
public class DefaultExplorerTreeModel extends AbstractTreeModel<NavigationNode>
                implements RefreshableTreeModel<NavigationNode>, Cloneable, Sortable<NavigationNode>
{
    public static final String ZK_SORT_ASCENDING = "ascending";
    public static final String ZK_SORT_DESCENDING = "descending";
    private final transient Map<Object, Set<NavigationNode>> nodeDataCache = new ConcurrentHashMap<>();
    private final transient NavigationTreeModelGetPathOptimizer pathOptimizer = new NavigationTreeModelGetPathOptimizer();
    private transient NavigationTree navTree;
    private boolean ascending = true;


    public DefaultExplorerTreeModel(final NavigationTree navTree)
    {
        super(navTree == null || navTree.getRootNodes() == null || navTree.getRootNodes().isEmpty() ? null
                        : navTree.getRootNodes().iterator().next());
        this.navTree = navTree;
    }


    @Override
    public Object clone()
    {
        final DefaultExplorerTreeModel clone = (DefaultExplorerTreeModel)super.clone();
        clone.navTree = this.navTree;
        clone.setMultiple(this.isMultiple());
        clone.addSelectionPaths(this.getSelectionPaths());
        clone.addOpenPaths(this.getOpenPaths());
        return clone;
    }


    @Override
    public NavigationNode getChild(final NavigationNode parent, final int index)
    {
        NavigationNode ret = null;
        final List<NavigationNode> children = findChildren(parent);
        if(CollectionUtils.isNotEmpty(children) && children.size() > index)
        {
            ret = children.get(index);
        }
        return ret;
    }


    protected List<NavigationNode> findChildren(final NavigationNode parent)
    {
        if(parent.getData() != null)
        {
            populateNodeDataCache(parent);
        }
        return parent.getChildren();
    }


    protected void populateNodeDataCache(final NavigationNode node)
    {
        nodeDataCache.computeIfAbsent(node.getData(), n -> new HashSet<>()).add(node);
    }


    @Override
    public int getChildCount(final NavigationNode parent)
    {
        if(parent == null)
        {
            return 0;
        }
        return CollectionUtils.size(findChildren(parent));
    }


    @Override
    public int getIndexOfChild(final NavigationNode parent, final NavigationNode child)
    {
        int ret = -1;
        final List<NavigationNode> children = findChildren(parent);
        if(CollectionUtils.isNotEmpty(children))
        {
            for(int i = 0; i < children.size(); i++)
            {
                if(child == children.get(i))
                {
                    ret = i;
                    break;
                }
            }
        }
        return ret;
    }


    @Override
    public NavigationNode getRoot()
    {
        if(this.navTree == null)
        {
            return null;
        }
        if(CollectionUtils.isEmpty(this.navTree.getRootNodes()))
        {
            return null;
        }
        return this.navTree.getRootNodes().iterator().next();
    }


    @Override
    public boolean isLeaf(final NavigationNode node)
    {
        return CollectionUtils.isEmpty(findChildren(node));
    }


    protected List<NavigationNode> getChildren(final NavigationNode node)
    {
        return findChildren(node);
    }


    @Override
    public void refreshChildren(final NavigationNode parent, final List<NavigationNode> children)
    {
        nodeDataCache.remove(parent.getData());
        children.stream().map(NavigationNode::getData).forEach(nodeDataCache::remove);
    }


    @Override
    public void refreshNodeCache(final NavigationNode node)
    {
        if(node.getData() != null)
        {
            populateNodeDataCache(node);
        }
    }


    @Override
    public List<NavigationNode> findNodesByData(final Object data)
    {
        final Set<NavigationNode> nodes = nodeDataCache.get(data);
        final List<NavigationNode> nodesWithWrappedData = findNodesWithWrappedData(data);
        return CollectionUtils.union(SetUtils.emptyIfNull(nodes), ListUtils.emptyIfNull(nodesWithWrappedData)).stream().distinct()
                        .collect(Collectors.toList());
    }


    protected List<NavigationNode> findNodesWithWrappedData(final Object data)
    {
        return nodeDataCache.entrySet().stream().filter(entry -> entry.getKey() instanceof ObjectWithComponentContext)
                        .filter(entry -> ((ObjectWithComponentContext)entry.getKey()).getInputObject().equals(data)).map(Map.Entry::getValue)
                        .flatMap(Collection::stream).collect(Collectors.toList());
    }


    @Override
    public int[] getPath(final NavigationNode child)
    {
        return pathOptimizer.getPath(child);
    }


    /**
     * @deprecated since 1808, is no longer used
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected int indexOf(final NavigationNode node, final List<NavigationNode> children)
    {
        for(int index = 0; index < children.size(); index++)
        {
            final NavigationNode child = children.get(index);
            final NavigationNode nodeUnpacked = unpackNavigationNodeDecorator(node);
            final NavigationNode childUnpacked = unpackNavigationNodeDecorator(child);
            if(Objects.equals(nodeUnpacked, childUnpacked))
            {
                return index;
            }
        }
        return -1;
    }


    /**
     * @deprecated since 1808, is no longer used
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected NavigationNode unpackNavigationNodeDecorator(final NavigationNode node)
    {
        return NavigationNodeDecoratorUtils.unwrap(node);
    }


    @Override
    public void sort(final Comparator<NavigationNode> comparator, final boolean ascending)
    {
        this.ascending = ascending;
        this.sortRecursively(getRoot(), comparator);
    }


    private void sortRecursively(final NavigationNode node, final Comparator<NavigationNode> comparator)
    {
        node.getChildren().sort(comparator);
        node.getChildren().forEach(n -> sortRecursively(n, comparator));
    }


    @Override
    public String getSortDirection(final Comparator comparator)
    {
        return ascending ? ZK_SORT_ASCENDING : ZK_SORT_DESCENDING;
    }
}
