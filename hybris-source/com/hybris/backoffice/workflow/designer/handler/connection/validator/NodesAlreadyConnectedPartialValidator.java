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
 * Checks if two nodes of added edge is already connected
 */
public class NodesAlreadyConnectedPartialValidator implements WorkflowConnectionPartialValidator
{
    public static final String EVENT_TYPE_ALREADY_CONNECTED = "alreadyConnected";
    private ConnectionFinder connectionFinder;
    private NodeTypeService nodeTypeService;
    private int order = MEDIUM_PRECEDENCE;


    @Override
    public WorkflowConnectionValidationResult validate(final ValidationContext context)
    {
        final NetworkChartContext networkChartContext = context.getNetworkChartContext();
        final Edge edge = context.getEdge();
        if(areNodesAlreadyConnected(networkChartContext, edge))
        {
            final String fromNodeName = getNodeTypeService().getNodeName(edge.getFromNode());
            final String toNodeName = getNodeTypeService().getNodeName(edge.getToNode());
            return WorkflowConnectionValidationResult
                            .ofViolations(Violation.create(EVENT_TYPE_ALREADY_CONNECTED, fromNodeName, toNodeName));
        }
        return WorkflowConnectionValidationResult.EMPTY;
    }


    protected boolean areNodesAlreadyConnected(final NetworkChartContext networkChartContext, final Edge edge)
    {
        final Node fromNode = edge.getFromNode();
        final Node toNode = edge.getToNode();
        return getConnectionFinder().areNodesConnected(networkChartContext, fromNode, toNode)
                        || getConnectionFinder().areNodesConnected(networkChartContext, toNode, fromNode);
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
