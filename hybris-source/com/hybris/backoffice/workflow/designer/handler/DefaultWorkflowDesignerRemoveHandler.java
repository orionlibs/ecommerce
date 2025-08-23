/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerNetworkPopulator;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Edges;
import com.hybris.cockpitng.components.visjs.network.data.NetworkEntity;
import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import com.hybris.cockpitng.components.visjs.network.response.Action;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdate;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of remove action in workflow designer
 */
public class DefaultWorkflowDesignerRemoveHandler implements WorkflowDesignerRemoveHandler
{
    private NodeTypeService nodeTypeService;


    @Override
    public NetworkUpdates remove(final Nodes nodes, final NetworkChartContext context)
    {
        removeNodesFromWidgetModel(nodes, context);
        return convertRemovedEntitiesToNetworkUpdates(nodes.getNodes());
    }


    @Override
    public NetworkUpdates remove(final Edges edges, final NetworkChartContext context)
    {
        return convertRemovedEntitiesToNetworkUpdates(edges.getEdges());
    }


    /**
     * Removes nodes from the {@link WorkflowDesignerNetworkPopulator#MODEL_NEW_WORKFLOW_ITEMS_KEY}
     *
     * @param nodes
     *           to be removed from model
     * @param context
     *           context containing model
     */
    protected void removeNodesFromWidgetModel(final Nodes nodes, final NetworkChartContext context)
    {
        getNewWorkflowItemsFromWidgetModel(context).removeIf(item -> {
            if(item instanceof WorkflowActionTemplateModel)
            {
                return nodes.getNodes().stream()
                                .anyMatch(node -> nodeTypeService.isSameAction((WorkflowActionTemplateModel)item, node));
            }
            if(item instanceof WorkflowDecisionTemplateModel)
            {
                return nodes.getNodes().stream()
                                .anyMatch(node -> nodeTypeService.isSameDecision((WorkflowDecisionTemplateModel)item, node));
            }
            return false;
        });
    }


    protected List<ItemModel> getNewWorkflowItemsFromWidgetModel(final NetworkChartContext context)
    {
        return Optional
                        .ofNullable(
                                        context.getWim().getModel().getValue(WorkflowDesignerNetworkPopulator.MODEL_NEW_WORKFLOW_ITEMS_KEY, List.class))
                        .orElseGet(Collections::emptyList);
    }


    protected NetworkUpdates convertRemovedEntitiesToNetworkUpdates(final Collection<? extends NetworkEntity> entities)
    {
        return new NetworkUpdates(
                        entities.stream().map(node -> new NetworkUpdate(Action.REMOVE, node)).collect(Collectors.toList()));
    }


    @Required
    public void setNodeTypeService(final NodeTypeService nodeTypeService)
    {
        this.nodeTypeService = nodeTypeService;
    }
}
