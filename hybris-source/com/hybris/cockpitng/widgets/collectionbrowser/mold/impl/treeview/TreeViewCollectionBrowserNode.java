/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.treeview;

import java.util.List;

/**
 * Tree node class for CollectionBrowser set to TreeView mold
 */
public class TreeViewCollectionBrowserNode extends AbstractTreeViewNode<TreeViewCollectionBrowserNode>
{
    public TreeViewCollectionBrowserNode(final TreeViewCollectionBrowserHelper helper, final Object data, final boolean accessible)
    {
        super(helper, data, accessible);
    }


    public TreeViewCollectionBrowserNode(final TreeViewCollectionBrowserHelper helper, final Object data,
                    final boolean accessible, final TreeViewCollectionBrowserNode parent)
    {
        super(helper, data, accessible, parent);
    }


    public TreeViewCollectionBrowserNode(final TreeViewCollectionBrowserHelper helper, final Object data,
                    final boolean accessible, final TreeViewCollectionBrowserNode parent, final List<TreeViewCollectionBrowserNode> children)
    {
        super(helper, data, accessible, parent, children);
    }


    @Override
    protected TreeViewCollectionBrowserNode createChild(final TreeViewCollectionBrowserHelper helper, final Object data,
                    final boolean accessible, final List<TreeViewCollectionBrowserNode> children)
    {
        return new TreeViewCollectionBrowserNode(helper, data, accessible, this, children);
    }
}
