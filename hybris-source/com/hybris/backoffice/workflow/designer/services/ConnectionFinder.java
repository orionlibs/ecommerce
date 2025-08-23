/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.services;

import com.google.common.collect.Sets;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

/**
 * Utility methods for operating on Workflow Designer connections.
 */
public class ConnectionFinder
{
    private NodeTypeService nodeTypeService;
    private NetworkEntityFinder networkEntityFinder;


    /**
     * Searches through the NetworkChart's edges, and finds the one coming from given action to given decision.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param actionModel
     *           action where the edge starts
     * @param decisionModel
     *           decision where edge ends
     * @return edge from given action to decision
     */
    public Optional<Edge> findActionToDecisionEdge(final NetworkChartContext context,
                    final WorkflowActionTemplateModel actionModel, final WorkflowDecisionTemplateModel decisionModel)
    {
        return networkEntityFinder.findEdges(context).stream()
                        .filter(edge -> nodeTypeService.isSameAction(actionModel, edge.getFromNode())
                                        && nodeTypeService.isSameDecision(decisionModel, edge.getToNode()))
                        .findFirst();
    }


    /**
     * Searches through the NetworkChart's edges, and finds the one coming from given decision to given action.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param decisionModel
     *           decision where the edge starts
     * @param actionModel
     *           action where the edge ends
     * @return edge from given decision to action
     */
    public Optional<Edge> findDecisionToActionEdge(final NetworkChartContext context,
                    final WorkflowDecisionTemplateModel decisionModel, final WorkflowActionTemplateModel actionModel)
    {
        return networkEntityFinder.findEdges(context).stream()
                        .filter(edge -> nodeTypeService.isSameDecision(decisionModel, edge.getFromNode())
                                        && nodeTypeService.isSameAction(actionModel, edge.getToNode()))
                        .findFirst();
    }


    /**
     * Checks if given decision is connected to given action through and 'And' node.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param decisionModel
     *           starting decision
     * @param actionModel
     *           ending action
     * @return true if decision is connected to action through an 'And' node
     */
    public boolean isDecisionConnectedToActionThroughAnd(final NetworkChartContext context,
                    final WorkflowDecisionTemplateModel decisionModel, final WorkflowActionTemplateModel actionModel)
    {
        return networkEntityFinder.findEdges(context).stream()
                        .anyMatch(edge -> nodeTypeService.isSameDecision(decisionModel, edge.getFromNode())
                                        && nodeTypeService.isAnd(edge.getToNode()) && findEdgesFromNode(context, edge.getToNode()).stream()
                                        .anyMatch(e -> nodeTypeService.isSameAction(actionModel, e.getToNode())));
    }


    /**
     * Filters all NetworkChart edges and returns only those, that come from action node and end in decision node.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return edges from action to decision
     */
    public Set<Edge> findActionToDecisionEdges(final NetworkChartContext context)
    {
        return networkEntityFinder.findEdges(context).stream()
                        .filter(edge -> nodeTypeService.isAction(edge.getFromNode()) && nodeTypeService.isDecision(edge.getToNode()))
                        .collect(Collectors.toSet());
    }


    /**
     * Filters all NetworkChart edges and returns only those, that come from decision node and end in action node.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return edges from decision to action
     */
    public Set<Edge> findDecisionToActionEdges(final NetworkChartContext context)
    {
        return networkEntityFinder.findEdges(context).stream()
                        .filter(edge -> nodeTypeService.isDecision(edge.getFromNode()) && nodeTypeService.isAction(edge.getToNode()))
                        .collect(Collectors.toSet());
    }


    /**
     * Finds edges incoming to node.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param node
     *           node for which the edges should be found
     * @return edges incoming to node
     */
    public Set<Edge> findEdgesToNode(final NetworkChartContext context, final Node node)
    {
        return networkEntityFinder.findEdges(context).stream().filter(edge -> edge.getToNode().equals(node))
                        .collect(Collectors.toSet());
    }


    /**
     * Finds edges outgoing from node.
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param node
     *           node for which the edges should be found
     * @return edges outgoing from node
     */
    public Set<Edge> findEdgesFromNode(final NetworkChartContext context, final Node node)
    {
        return networkEntityFinder.findEdges(context).stream().filter(edge -> edge.getFromNode().equals(node))
                        .collect(Collectors.toSet());
    }


    /**
     * Finds all edges connected with the node (both incoming and outgoing).
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @param node
     *           node for which the edges should be found
     * @return edges connected with node
     */
    public Set<Edge> findEdgesOfNode(final NetworkChartContext context, final Node node)
    {
        return Sets.union(findEdgesFromNode(context, node), findEdgesToNode(context, node));
    }


    /**
     * Checks if given nodes are directly connected
     *
     * @param context
     *           contains the {@link WidgetInstanceManager} of the Workflow Designer
     * @param sourceNode
     *           {@link Edge#fromNode} of the edge
     * @param targetNode
     *           {@link Edge#toNode} of the edge
     * @return true if nodes are directly connected
     */
    public boolean areNodesConnected(final NetworkChartContext context, final Node sourceNode, final Node targetNode)
    {
        return findEdgesFromNode(context, sourceNode).stream().anyMatch(edge -> edge.getToNode().equals(targetNode));
    }


    @Required
    public void setNodeTypeService(final NodeTypeService nodeTypeService)
    {
        this.nodeTypeService = nodeTypeService;
    }


    @Required
    public void setNetworkEntityFinder(final NetworkEntityFinder networkEntityFinder)
    {
        this.networkEntityFinder = networkEntityFinder;
    }
}
