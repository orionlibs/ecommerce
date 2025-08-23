/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.services;

import com.hybris.backoffice.widgets.networkchart.NetworkChartController;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

/**
 * Utility methods for finding {@link com.hybris.cockpitng.components.visjs.network.data.NetworkEntity} from Workflow
 * Designer nodes.
 */
public class NetworkEntityFinder
{
    private NodeTypeService nodeTypeService;


    /**
     * Gets all action nodes
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return action nodes
     */
    public List<Node> findActionNodes(final NetworkChartContext context)
    {
        return findNodes(context).stream().filter(getNodeTypeService()::isAction).collect(Collectors.toList());
    }


    /**
     * Gets all decision nodes
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return decision nodes
     */
    public List<Node> findDecisionNodes(final NetworkChartContext context)
    {
        return findNodes(context).stream().filter(getNodeTypeService()::isDecision).collect(Collectors.toList());
    }


    /**
     * Gets all 'and' nodes
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return 'and' nodes
     */
    public List<Node> findAndNodes(final NetworkChartContext context)
    {
        return findNodes(context).stream().filter(getNodeTypeService()::isAnd).collect(Collectors.toList());
    }


    /**
     * Gets all {@link Node}s in the Workflow Designer
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return nodes
     */
    public Collection<Node> findNodes(final NetworkChartContext context)
    {
        return context.getWim().getModel().getValue(NetworkChartController.MODEL_NETWORK_NODES, Collection.class);
    }


    /**
     * Gets all {@link Edge}s in the Workflow Designer
     *
     * @param context
     *           contains Network Chart widget model, where the Workflow Designer data is stored
     * @return edges
     */
    public Collection<Edge> findEdges(final NetworkChartContext context)
    {
        return context.getWim().getModel().getValue(NetworkChartController.MODEL_NETWORK_EDGES, Collection.class);
    }


    /**
     * Returns {@link Node} that corresponds the given {@link WorkflowActionTemplateModel}
     */
    public Optional<Node> findActionNode(final NetworkChartContext context, final WorkflowActionTemplateModel action)
    {
        return findActionNodes(context).stream().filter(node -> getNodeTypeService().isSameAction(action, node)).findFirst();
    }


    /**
     * Returns {@link Node} that corresponds the given {@link WorkflowDecisionTemplateModel}
     */
    public Optional<Node> findDecisionNode(final NetworkChartContext context, final WorkflowDecisionTemplateModel decision)
    {
        return findDecisionNodes(context).stream().filter(node -> getNodeTypeService().isSameDecision(decision, node)).findFirst();
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
}
