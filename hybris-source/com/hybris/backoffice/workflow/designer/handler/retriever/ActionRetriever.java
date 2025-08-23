/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.retriever;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.backoffice.workflow.designer.services.WorkflowModelFinder;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of {@link WorkflowModelRetriever} which retrieves {@link WorkflowActionTemplateModel}
 */
public class ActionRetriever implements WorkflowModelRetriever<WorkflowActionTemplateModel>
{
    private static final Collection<String> ACTION_GROUPS = Set.of(WorkflowDesignerGroup.END_ACTION.getValue(),
                    WorkflowDesignerGroup.START_ACTION.getValue(), WorkflowDesignerGroup.ACTION.getValue());
    private NodeTypeService nodeTypeService;
    private WorkflowModelFinder workflowModelFinder;


    @Override
    public Optional<WorkflowActionTemplateModel> retrieve(final Node node, final NetworkChartContext context)
    {
        if(!ACTION_GROUPS.contains(node.getGroup()))
        {
            return Optional.empty();
        }
        return workflowModelFinder.findNewWorkflowAction(context, node).or(() -> retrieveModelFromExistingTemplate(node, context));
    }


    private Optional<WorkflowActionTemplateModel> retrieveModelFromExistingTemplate(final Node node,
                    final NetworkChartContext context)
    {
        return workflowModelFinder.findWorkflowActionsFromWorkflowTemplateModel(context).stream()
                        .filter(e -> nodeTypeService.isSameAction(e, node)).findFirst();
    }


    @Required
    public void setNodeTypeService(final NodeTypeService nodeTypeService)
    {
        this.nodeTypeService = nodeTypeService;
    }


    @Required
    public void setWorkflowModelFinder(final WorkflowModelFinder workflowModelFinder)
    {
        this.workflowModelFinder = workflowModelFinder;
    }
}
