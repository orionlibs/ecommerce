/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowConnection;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link WorkflowConnectionPartialValidator}
 */
public class AvailableConnectionPartialValidator implements WorkflowConnectionPartialValidator
{
    public static final String EVENT_TYPE_WRONG_CONNECTION = "wrongConnection";
    private List<WorkflowConnection> availableConnections = Collections.emptyList();
    private int order = MEDIUM_PRECEDENCE;


    @Override
    public WorkflowConnectionValidationResult validate(final ValidationContext context)
    {
        final Edge edge = context.getEdge();
        if(getAvailableConnections().contains(WorkflowConnection.of(edge)))
        {
            return WorkflowConnectionValidationResult.EMPTY;
        }
        return WorkflowConnectionValidationResult.ofViolations(
                        Violation.create(EVENT_TYPE_WRONG_CONNECTION, edge.getFromNode().getGroup(), edge.getToNode().getGroup()));
    }


    public List<WorkflowConnection> getAvailableConnections()
    {
        return availableConnections;
    }


    @Required
    public void setAvailableConnections(final List<WorkflowConnection> availableConnections)
    {
        this.availableConnections = availableConnections;
    }


    @Override
    public int getOrder()
    {
        return order;
    }


    // optional
    public void setOrder(final int order)
    {
        this.order = order;
    }
}
