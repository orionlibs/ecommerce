/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.retriever;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.backoffice.workflow.designer.services.WorkflowModelFinder;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of {@link WorkflowModelRetriever} which retrieves {@link WorkflowDecisionTemplateModel}
 */
public class DecisionRetriever implements WorkflowModelRetriever<WorkflowDecisionTemplateModel>
{
    private NodeTypeService nodeTypeService;
    private WorkflowModelFinder workflowModelFinder;


    @Override
    public Optional<WorkflowDecisionTemplateModel> retrieve(final Node node, final NetworkChartContext context)
    {
        if(!StringUtils.equals(node.getGroup(), WorkflowDesignerGroup.DECISION.getValue()))
        {
            return Optional.empty();
        }
        return workflowModelFinder.findNewWorkflowDecision(context, node)
                        .or(() -> retrieveModelFromExistingTemplate(node, context));
    }


    private Optional<WorkflowDecisionTemplateModel> retrieveModelFromExistingTemplate(final Node node,
                    final NetworkChartContext context)
    {
        return workflowModelFinder.findWorkflowDecisionsFromWorkflowTemplateModel(context).stream()
                        .filter(decision -> nodeTypeService.isSameDecision(decision, node)).findFirst();
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
