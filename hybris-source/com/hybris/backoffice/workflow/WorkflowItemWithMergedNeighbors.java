/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class WorkflowItemWithMergedNeighbors extends WorkflowItem
{
    private final WorkflowItem target;
    private final WorkflowItem source;


    public WorkflowItemWithMergedNeighbors(final WorkflowItem target, final WorkflowItem source)
    {
        super(target.getId(), target.getType(), target.isEnd());
        this.target = target;
        this.source = source;
    }


    @Override
    public ItemModel getModel()
    {
        return target.getModel();
    }


    @Override
    public Collection<String> getNeighborsIds()
    {
        final Set<String> neighborsIds = new HashSet<>(target.getNeighborsIds());
        neighborsIds.addAll(source.getNeighborsIds());
        return neighborsIds;
    }


    @Override
    public Node createNode()
    {
        return target.createNode();
    }


    @Override
    public Integer getLevel()
    {
        return target.getLevel();
    }


    @Override
    public void setLevel(final Integer level)
    {
        target.setLevel(level);
    }


    @Override
    public boolean equals(final Object o)
    {
        return super.equals(o);
    }


    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
}
