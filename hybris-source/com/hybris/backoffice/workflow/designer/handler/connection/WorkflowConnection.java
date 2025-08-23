/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection;

import com.hybris.cockpitng.components.visjs.network.data.Edge;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

/**
 * A POJO class which encapsulates DESTINATION and TARGET of Workflow's connection
 */
public class WorkflowConnection
{
    public static final WorkflowConnection EMPTY = new WorkflowConnection(WorkflowDesignerGroup.UNKNOWN,
                    WorkflowDesignerGroup.UNKNOWN);
    private final WorkflowDesignerGroup from;
    private final WorkflowDesignerGroup to;


    private WorkflowConnection(final WorkflowDesignerGroup from, final WorkflowDesignerGroup to)
    {
        this.from = from;
        this.to = to;
    }


    /**
     * Allows to create an instance of {@link WorkflowConnection} using {@link WorkflowDesignerGroup}
     *
     * @param from
     *           destination of connection
     * @param to
     *           target of connection
     * @return new instance of {@link WorkflowDesignerGroup}
     */
    public static WorkflowConnection of(final WorkflowDesignerGroup from, final WorkflowDesignerGroup to)
    {
        return new WorkflowConnection(from, to);
    }


    /**
     * Allows to create an instance of {@link WorkflowConnection} using {@link Edge}
     *
     * @param edge
     *           connection is created based on {@link Edge#getFromNode()} and {@link Edge#getToNode()}
     * @return new instance of {@link WorkflowDesignerGroup}
     */
    public static WorkflowConnection of(final Edge edge)
    {
        final WorkflowDesignerGroup left = Stream.of(WorkflowDesignerGroup.values())
                        .filter(group -> StringUtils.equals(group.getValue(), edge.getFromNode().getGroup())).findFirst()
                        .orElse(WorkflowDesignerGroup.UNKNOWN);
        final WorkflowDesignerGroup to = Stream.of(WorkflowDesignerGroup.values())
                        .filter(group -> StringUtils.equals(group.getValue(), edge.getToNode().getGroup())).findFirst()
                        .orElse(WorkflowDesignerGroup.UNKNOWN);
        return WorkflowConnection.of(left, to);
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
        final WorkflowConnection that = (WorkflowConnection)o;
        return from == that.from && to == that.to;
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(from, to);
    }


    public WorkflowDesignerGroup getFrom()
    {
        return from;
    }


    public WorkflowDesignerGroup getTo()
    {
        return to;
    }
}
