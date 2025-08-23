/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.services.ConnectionFinder;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

/**
 * Validates if connection via 'and' node is not duplicated, for example: <code><pre>
 *     node1 --> (and) --> end <-- (and) <-- node2
 * </pre></code> is disallowed and user should reuse existing 'and' node.
 */
public class DuplicateConnectionViaAndPartialValidator implements WorkflowConnectionPartialValidator
{
    public static final String EVENT_TYPE_MULTIPLE_AND_NODES_POINTING_TO_SAME_TARGET = "multipleAndNodesPointingToSameTarget";
    private ConnectionFinder connectionFinder;
    private NodeTypeService nodeTypeService;
    private int order = MEDIUM_PRECEDENCE;


    @Override
    public WorkflowConnectionValidationResult validate(final ValidationContext context)
    {
        final NetworkChartContext networkChartContext = context.getNetworkChartContext();
        final Node sourceNode = context.getEdge().getFromNode();
        final Node targetNode = context.getEdge().getToNode();
        if(!getNodeTypeService().isAnd(sourceNode))
        {
            return WorkflowConnectionValidationResult.EMPTY;
        }
        final Optional<Node> andPointingToTargetNode = getConnectionFinder().findEdgesToNode(networkChartContext, targetNode)
                        .stream().map(Edge::getFromNode).filter(getNodeTypeService()::isAnd).findFirst();
        if(andPointingToTargetNode.isEmpty())
        {
            return WorkflowConnectionValidationResult.EMPTY;
        }
        final String nodeName = getNodeTypeService().getNodeName(targetNode);
        return WorkflowConnectionValidationResult
                        .ofViolations(Violation.create(EVENT_TYPE_MULTIPLE_AND_NODES_POINTING_TO_SAME_TARGET, nodeName));
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
