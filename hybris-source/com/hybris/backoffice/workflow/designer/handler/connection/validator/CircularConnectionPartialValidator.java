/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.services.ConnectionFinder;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

/**
 * Validates if created connection between action and decision does not create a circular connection. It's not possible
 * to create both: <code><pre>
 * decision > action
 * </pre></code> and:<code><pre>
 * decision > and > action
 * </pre></code> connection.
 */
public class CircularConnectionPartialValidator implements WorkflowConnectionPartialValidator
{
    public static final String EVENT_TYPE_NODES_DIRECTLY_CONNECTED = "nodesAlreadyConnectedDirectly";
    public static final String EVENT_TYPE_ALREADY_CONNECTED_VIA_AND = "alreadyConnectedViaAnd";
    private NodeTypeService nodeTypeService;
    private ConnectionFinder connectionFinder;
    private int order = MEDIUM_PRECEDENCE;


    @Override
    public WorkflowConnectionValidationResult validate(final ValidationContext context)
    {
        final NetworkChartContext networkChartContext = context.getNetworkChartContext();
        final Edge edge = context.getEdge();
        return detectCircularConnection(networkChartContext, edge);
    }


    /**
     * Checks if two nodes of added edge have circular connection via 'and' connector <br>
     * Circular connection: decision -> action, decision -> 'and' connector and 'and' connector -> action and
     *
     * @param edge
     *           edge that was added
     * @param context
     *           contains the {@link WidgetInstanceManager} of the Workflow Designer
     * @return result with appropriate violation if nodes have circular connection via 'and' connector
     */
    protected WorkflowConnectionValidationResult detectCircularConnection(final NetworkChartContext context, final Edge edge)
    {
        return WorkflowConnectionValidationResult.concat(detectDecisionConnectedWithActionInCircularConnection(context, edge),
                        detectNodesConnectedViaAndConnector(context, edge));
    }


    /**
     * Checks if added edge has connection from decision node to 'and' connector or from 'and' connector to action node.
     * Then it checks again if new edge introduce circular connection via 'and' connector. <br>
     * Circular connection: decision -> action, decision -> 'and' connector and 'and' connector -> action and
     *
     * @param edge
     *           edge that was added
     * @param context
     *           contains the {@link WidgetInstanceManager} of the Workflow Designer
     * @return result with appropriate violation if source {@link Node} of 'and' connector is directly connected with target
     *         {@link Edge#toNode}
     */
    protected WorkflowConnectionValidationResult detectDecisionConnectedWithActionInCircularConnection(
                    final NetworkChartContext context, final Edge edge)
    {
        final String fromNodeName = getNodeName(edge.getFromNode());
        final String toNodeName = getNodeName(edge.getToNode());
        if(getNodeTypeService().isAnd(edge.getFromNode()))
        {
            final Node andNode = edge.getFromNode();
            final Node targetAction = edge.getToNode();
            final Optional<Edge> decisionToAndEdge = getConnectionFinder().findEdgesToNode(context, andNode).stream()
                            .filter(foundEdge -> getConnectionFinder().areNodesConnected(context, foundEdge.getFromNode(), targetAction))
                            .findFirst();
            if(decisionToAndEdge.isPresent())
            {
                final String decisionName = getNodeName(decisionToAndEdge.get().getFromNode());
                return WorkflowConnectionValidationResult.ofViolations(
                                Violation.create(EVENT_TYPE_NODES_DIRECTLY_CONNECTED, fromNodeName, toNodeName, decisionName, toNodeName));
            }
        }
        else if(getNodeTypeService().isAnd(edge.getToNode()))
        {
            final Node andNode = edge.getToNode();
            final Node sourceDecision = edge.getFromNode();
            final Optional<Edge> andToDecisionEdge = getConnectionFinder().findEdgesFromNode(context, andNode).stream()
                            .filter(foundEdge -> getConnectionFinder().areNodesConnected(context, sourceDecision, foundEdge.getToNode()))
                            .findFirst();
            if(andToDecisionEdge.isPresent())
            {
                final String actionName = getNodeName(andToDecisionEdge.get().getToNode());
                return WorkflowConnectionValidationResult.ofViolations(
                                Violation.create(EVENT_TYPE_NODES_DIRECTLY_CONNECTED, fromNodeName, toNodeName, fromNodeName, actionName));
            }
        }
        return WorkflowConnectionValidationResult.EMPTY;
    }


    /**
     * Checks if {@link Edge#fromNode} and {@link Edge#toNode} of added edge is already connected via 'and' connector node
     *
     * @param edge
     *           edge that was added
     * @param context
     *           contains the {@link WidgetInstanceManager} of the Workflow Designer
     * @return result with appropriate violation if {@link Edge#fromNode} and {@link Edge#toNode} are already connected by
     *         'and' connector
     */
    protected WorkflowConnectionValidationResult detectNodesConnectedViaAndConnector(final NetworkChartContext context,
                    final Edge edge)
    {
        final Set<Edge> sourceDecisionEdges = getConnectionFinder().findEdgesFromNode(context, edge.getFromNode());
        final Set<Edge> targetActionEdges = getConnectionFinder().findEdgesToNode(context, edge.getToNode());
        final boolean isConnectedViaAnd = targetActionEdges.stream()
                        .filter(actionEdge -> getNodeTypeService().isAnd(actionEdge.getFromNode()))
                        .anyMatch(actionEdge -> sourceDecisionEdges.stream()
                                        .filter(decisionEdge -> getNodeTypeService().isAnd(decisionEdge.getToNode()))
                                        .anyMatch(decisionEdge -> decisionEdge.getToNode().equals(actionEdge.getFromNode())));
        if(isConnectedViaAnd)
        {
            final String fromName = getNodeName(edge.getFromNode());
            final String toName = getNodeName(edge.getToNode());
            return WorkflowConnectionValidationResult
                            .ofViolations(Violation.create(EVENT_TYPE_ALREADY_CONNECTED_VIA_AND, fromName, toName));
        }
        return WorkflowConnectionValidationResult.EMPTY;
    }


    private String getNodeName(final Node fromNode)
    {
        return getNodeTypeService().getNodeName(fromNode);
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


    public ConnectionFinder getConnectionFinder()
    {
        return connectionFinder;
    }


    @Required
    public void setConnectionFinder(final ConnectionFinder connectionFinder)
    {
        this.connectionFinder = connectionFinder;
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
