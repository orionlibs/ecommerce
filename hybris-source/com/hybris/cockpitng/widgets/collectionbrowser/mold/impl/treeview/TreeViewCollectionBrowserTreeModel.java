/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.treeview;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.zkoss.zul.AbstractTreeModel;

/**
 * Tree model class for Collection Browser widget. Parametrized with node type.
 */
public class TreeViewCollectionBrowserTreeModel<N extends TreeViewCollectionBrowserNode> extends AbstractTreeModel<N>
{
    public static final int AUTO_EXPAND_CHILDREN_COUNT = 1;
    private final List<N> topLevelNodes;


    public TreeViewCollectionBrowserTreeModel(final List<N> topLevelNodes)
    {
        super(null);
        super.setSelectionControl(new TreeViewCollectionBrowserSelectionControl());
        this.topLevelNodes = topLevelNodes;
    }


    public TreeViewCollectionBrowserTreeModel(final TreeViewCollectionBrowserTreeModel model)
    {
        super(null);
        this.topLevelNodes = new ArrayList<>(model.topLevelNodes);
        this._selection = model._selection;
        this._opens = model._opens;
    }


    public List<N> getTopLevelNodes()
    {
        return topLevelNodes;
    }


    @Override
    public N getChild(final N parent, final int index)
    {
        if(parent != null)
        {
            if(parent.getChildrenCount() > index)
            {
                return (N)parent.getChildAtIndex(index);
            }
        }
        else if(index < topLevelNodes.size())
        {
            return topLevelNodes.get(index);
        }
        return null;
    }


    /*
     * Overridden because of incorrect implementation in AbstractTreeModel in ZK-8.0.0 To be removed when fixed by ZK team.
     */
    @Override
    public N getChild(final int[] path)
    {
        Object parent = this.getRoot();
        if(path.length == 0)
        {
            return (N)parent;
        }
        else
        {
            Object node = null;
            int childCount = this.getChildCount((N)parent);
            for(int i = 0; i < path.length; ++i)
            {
                if(path[i] < 0 || path[i] > childCount)
                {
                    return null;
                }
                node = this.getChild((N)parent, path[i]);
                if(node != null && (childCount = this.getChildCount((N)node)) > 0)
                {
                    parent = node;
                }
                else if(i != path.length - 1)
                {
                    return null;
                }
            }
            return (N)node;
        }
    }


    @Override
    public int getChildCount(final N parent)
    {
        return parent != null ? parent.getChildrenCount() : topLevelNodes.size();
    }


    @Override
    public boolean isLeaf(final N node)
    {
        return (getChildCount(node) == 0);
    }


    @Override
    public int[] getPath(final N node)
    {
        final List<Integer> path = new ArrayList<>(node.getLevel() + 1);
        N currentNode = node;
        while(currentNode.getParent() != null)
        {
            path.add(Integer.valueOf(currentNode.getParent().indexOfChild(currentNode)));
            currentNode = (N)currentNode.getParent();
        }
        N topLevelNode = currentNode;
        boolean belongsCurrentTree = false;
        for(int i = 0; i < topLevelNodes.size() && !belongsCurrentTree; i++)
        {
            if(topLevelNodes.get(i).getData().equals(topLevelNode.getData()))
            {
                path.add(i);
                belongsCurrentTree = true;
            }
        }
        if(!belongsCurrentTree)
        {
            path.clear();
        }
        return Ints.toArray(Lists.reverse(path));
    }


    @Override
    public boolean isPathOpened(final int[] path)
    {
        final N node = getChild(path);
        final TreeViewCollectionBrowserNode parentNode = node != null ? node.getParent() : null;
        if(parentNode != null && parentNode.getChildrenCount() == AUTO_EXPAND_CHILDREN_COUNT)
        {
            return true;
        }
        return super.isPathOpened(path);
    }


    /**
     * Gets tree node for specified data. Only already opened nodes are taken under consideration
     *
     * @param data
     *           data of node to be found
     * @return node found or <code>null</code> if there is no node assigned to provided data
     */
    public N getNode(final Object data)
    {
        return getNode(data, topLevelNodes);
    }


    protected N getNode(final Object data, final Collection<? extends TreeViewCollectionBrowserNode> nodes)
    {
        final Optional<? extends TreeViewCollectionBrowserNode> topLevel = nodes.stream()
                        .filter(node -> Objects.equals(data, node.getData())).findFirst();
        if(topLevel.isPresent())
        {
            return (N)topLevel.get();
        }
        else
        {
            final Optional<? extends N> child = nodes.stream().filter(node -> node.children != null)
                            .map(node -> getNode(data, node.children)).filter(node -> node != null).findFirst();
            return child.orElse(null);
        }
    }


    /**
     * Sets new section by specifying data to be selected (rather then nodes).
     *
     * @param selection
     *           data to be selected
     * @see #setSelection(Collection)
     */
    public void setDataSelection(final Collection<?> selection)
    {
        if(this.isDataSelectionChanged(selection))
        {
            setSelection(selection.stream().map(this::getNode).filter(node -> node != null).collect(Collectors.toList()));
        }
    }


    protected boolean isDataSelectionChanged(final Collection<?> selection)
    {
        if(this._selection.size() != selection.size())
        {
            return true;
        }
        else
        {
            return _selection.stream().anyMatch(path -> {
                final N child = getChild(path.path);
                return child == null || !selection.contains(child.getData());
            });
        }
    }


    protected class TreeViewCollectionBrowserSelectionControl extends DefaultSelectionControl<N>
    {
        public TreeViewCollectionBrowserSelectionControl()
        {
            super(TreeViewCollectionBrowserTreeModel.this);
        }


        @Override
        public boolean isSelectable(final N node)
        {
            return node.isSelectable();
        }
    }
}
