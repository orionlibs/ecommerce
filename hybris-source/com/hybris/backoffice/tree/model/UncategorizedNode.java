/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.tree.model;

import com.hybris.cockpitng.widgets.common.explorertree.data.ActionAwareNode;

/**
 * Model representing uncategorized node. The parentItem property contains parent node data of uncategorized node. For
 * example: parentItem will be equal to catalogVersionModel for uncategorized node in catalog version
 */
public class UncategorizedNode implements ActionAwareNode
{
    private final Object parentItem;


    public UncategorizedNode(final Object parentItem)
    {
        this.parentItem = parentItem;
    }


    public Object getParentItem()
    {
        return parentItem;
    }
}
