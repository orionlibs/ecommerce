/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents the nodes processed by the {@link WorkflowNetworkFactory}
 */
public abstract class WorkflowItem
{
    static final int BASE_LEVEL = 0;
    private final String id;
    private final boolean end;
    private final Type type;
    private Integer level;


    public WorkflowItem(final String id, final Type type, final boolean end)
    {
        this.id = id;
        this.level = BASE_LEVEL;
        this.type = type;
        this.end = end;
    }


    public String getId()
    {
        return id;
    }


    public Type getType()
    {
        return type;
    }


    public boolean isEnd()
    {
        return end;
    }


    public Integer getLevel()
    {
        return level;
    }


    public void setLevel(final Integer level)
    {
        this.level = level;
    }


    /**
     * Creates the {@link Node} object that will be drawn on the graph
     *
     * @return created node
     */
    public abstract Node createNode();


    public abstract ItemModel getModel();


    /**
     * Returns the collection of ids of the neighbors
     *
     * @return neighbors ids
     */
    public abstract Collection<String> getNeighborsIds();


    @Override
    public int hashCode()
    {
        return Objects.hashCode(getId());
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final WorkflowItem that = (WorkflowItem)o;
        return Objects.equals(getId(), that.getId());
    }


    /**
     * Represents the type of the node
     */
    public enum Type
    {
        DECISION, ACTION, AND_LINK
    }
}
