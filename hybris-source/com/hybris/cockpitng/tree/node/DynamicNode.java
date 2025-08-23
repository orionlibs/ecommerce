/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.node;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.cockpitng.tree.util.TreeUtils;
import com.hybris.cockpitng.widgets.common.explorertree.data.ActionAwareNode;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

/**
 * Represents a navigation node with dynamic children.
 */
public class DynamicNode extends SimpleNode implements ActionAwareNode
{
    private static final int DEFAULT_INDEXING_DEPTH = 0;
    private final transient DynamicNodePopulator populator;
    private final boolean rootNode;
    private final int indexingDepth;
    private final boolean searchable;
    private boolean selectable;
    private boolean actionAware = true;


    public DynamicNode(final String id, final DynamicNodePopulator populator)
    {
        this(id, false, populator, DEFAULT_INDEXING_DEPTH);
    }


    public DynamicNode(final String id, final boolean rootNode, final DynamicNodePopulator populator)
    {
        this(id, rootNode, populator, DEFAULT_INDEXING_DEPTH);
    }


    public DynamicNode(final String id, final DynamicNodePopulator populator, final int indexingDepth)
    {
        this(id, false, populator, indexingDepth);
    }


    public DynamicNode(final String id, final boolean rootNode, final DynamicNodePopulator populator, final int indexingDepth)
    {
        super(id);
        this.populator = populator;
        this.rootNode = rootNode;
        this.indexingDepth = indexingDepth;
        this.searchable = populator instanceof SearchableDynamicNodePopulator;
    }


    @Override
    public List<NavigationNode> getChildren()
    {
        if(CollectionUtils.isEmpty(super.getChildren()))
        {
            super.setChildren(populator.getChildren(this));
        }
        return super.getChildren();
    }


    @Override
    public List<NavigationNode> getChildren(final String filterString, final boolean caseSensitive,
                    final TreeUtils.MatchMode matchMode)
    {
        if(isSearchable())
        {
            final List<NavigationNode> children = ((SearchableDynamicNodePopulator)populator).getChildren(this, filterString, caseSensitive, matchMode);
            super.setChildren(children);
            return children;
        }
        throw new UnsupportedOperationException("Populator passed to the node is not searchable");
    }


    public boolean isSearchable()
    {
        return searchable;
    }


    public boolean isRootNode()
    {
        return rootNode;
    }


    public boolean isSelectable()
    {
        return selectable;
    }


    public void setSelectable(final boolean selectable)
    {
        this.selectable = selectable;
    }


    public int getIndexingDepth()
    {
        return indexingDepth;
    }


    @Override
    public boolean isActionAware()
    {
        return actionAware;
    }


    @Override
    public void setActionAware(final boolean actionAware)
    {
        this.actionAware = actionAware;
    }
}
