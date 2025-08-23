/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.services.ConnectionFinder;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import org.springframework.beans.factory.annotation.Required;

/**
 * Validates if connection from action to decision already exists
 */
public class MultipleConnectionsIncomingToDecisionPartialValidator implements WorkflowConnectionPartialValidator
{
    public static final String EVENT_TYPE_DECISION_CAN_HAVE_ONLY_ONE_INCOMING_CONNECTION = "decisionOneIncomingConnection";
    private ConnectionFinder connectionFinder;
    private NodeTypeService nodeTypeService;
    private int order = MEDIUM_PRECEDENCE;


    @Override
    public WorkflowConnectionValidationResult validate(final ValidationContext context)
    {
        final NetworkChartContext networkChartContext = context.getNetworkChartContext();
        final Edge edge = context.getEdge();
        final Node targetNode = edge.getToNode();
        if(!getNodeTypeService().isDecision(targetNode))
        {
            return WorkflowConnectionValidationResult.EMPTY;
        }
        if(getConnectionFinder().findEdgesToNode(networkChartContext, targetNode).isEmpty())
        {
            return WorkflowConnectionValidationResult.EMPTY;
        }
        final String nodeName = getNodeTypeService().getNodeName(targetNode);
        return WorkflowConnectionValidationResult
                        .ofViolations(Violation.create(EVENT_TYPE_DECISION_CAN_HAVE_ONLY_ONE_INCOMING_CONNECTION, nodeName));
    }


    public ConnectionFinder getConnectionFinder()
    {
        return connectionFinder;
    }


    @Required
    public void setConnectionFinder(final ConnectionFinder connectionFinder)
    {
        this.connectionFinder = connectionFinder;
    }


    public NodeTypeService getNodeTypeService()
    {
        return nodeTypeService;
    }


    @Required
    public void setNodeTypeService(final NodeTypeService nodeTypeService)
    {
        this.nodeTypeService = nodeTypeService;
    }


    // optional
    public void setOrder(final int order)
    {
        this.order = order;
    }


    @Override
    public int getOrder()
    {
        return order;
    }
}
