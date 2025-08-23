/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Validates each 'and' node. Each such node must have at least 2 incoming connection, and exactly 1 outgoing
 * connection.
 */
public class AndNodeValidator extends AbstractValidator
{
    public static final String AND_NODE_NEEDS_AT_LEAST_TWO_INCOMING_AND_ONE_OUTGOING_CONNECTIONS = "workflowdesigner.notification.andNodeNeedsAtLeastTwoIncomingAndOneOutgoingConnections.error";
    private static final int MINIMUM_NUMBER_OF_INCOMING_EDGES_TO_AND = 2;


    @Override
    public WorkflowDesignerValidationResult validate(final NetworkChartContext context)
    {
        final List<Violation> violations = checkAndHasTwoIncomingAndOneOutgoingConnection(context);
        return new WorkflowDesignerValidationResult(violations);
    }


    protected List<Violation> checkAndHasTwoIncomingAndOneOutgoingConnection(final NetworkChartContext context)
    {
        final List<Violation> violations = new ArrayList<>();
        getNetworkEntityFinder().findAndNodes(context).forEach(andNode -> {
            final Collection<Edge> incomingEdges = getConnectionFinder().findEdgesToNode(context, andNode);
            final Collection<Edge> outgoingEdges = getConnectionFinder().findEdgesFromNode(context, andNode);
            if(incomingEdges.size() < MINIMUM_NUMBER_OF_INCOMING_EDGES_TO_AND || outgoingEdges.isEmpty())
            {
                violations.add(Violation.error(AND_NODE_NEEDS_AT_LEAST_TWO_INCOMING_AND_ONE_OUTGOING_CONNECTIONS));
            }
        });
        return violations;
    }
}
