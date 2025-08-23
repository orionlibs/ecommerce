/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.services.NetworkEntityFinder;
import com.hybris.backoffice.workflow.designer.services.WorkflowModelFinder;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 * Creates Workflow Template data related to Actions, excluding links to decisions which is handled by
 * {@link LinkFromContextPreprocessor}.
 */
public class ActionFromContextPreprocessor implements NetworkChartContextPreprocessor
{
    private NetworkEntityFinder networkEntityFinder;
    private WorkflowModelFinder workflowModelFinder;


    /**
     * Creates data related to actions given in the context.
     *
     * @param context
     *           contains the Workflow Template data of the WorkflowDesigner widget
     */
    @Override
    public void preprocess(final NetworkChartContext context)
    {
        handleRemovedActions(context);
        handleNewOrUpdatedActions(context);
    }


    /**
     * Handles new actions to be initialized with {@link WorkflowTemplateModel} and updates position change
     */
    protected void handleNewOrUpdatedActions(final NetworkChartContext context)
    {
        final WorkflowTemplateModel workflowTemplate = getWorkflowModelFinder().findWorkflowTemplate(context);
        final List<WorkflowActionTemplateModel> actions = MutableListUtil.toMutableList(workflowTemplate.getActions());
        for(final Node node : getNetworkEntityFinder().findActionNodes(context))
        {
            getWorkflowModelFinder().findWorkflowActionInWorkflowTemplateModel(context, node)
                            .ifPresentOrElse(action -> updateActionPosition(node, action), () -> {
                                getWorkflowModelFinder().findNewWorkflowAction(context, node)
                                                .ifPresent(newAction -> handleNewAction(workflowTemplate, actions, node, newAction));
                            });
        }
        workflowTemplate.setActions(actions);
    }


    protected void handleNewAction(final WorkflowTemplateModel workflowTemplate, final List<WorkflowActionTemplateModel> actions,
                    final Node node, final WorkflowActionTemplateModel newAction)
    {
        updateActionPosition(node, newAction);
        newAction.setWorkflow(workflowTemplate);
        newAction.setIncomingTemplateDecisions(Collections.emptySet());
        newAction.setDecisionTemplates(Collections.emptySet());
        actions.add(newAction);
    }


    protected void updateActionPosition(final Node node, final WorkflowActionTemplateModel action)
    {
        action.setVisualisationX(node.getX());
        action.setVisualisationY(node.getY());
    }


    /**
     * Removes {@link WorkflowActionTemplateModel}s for which there is no {@link Node} (which means it was deleted).
     */
    protected void handleRemovedActions(final NetworkChartContext context)
    {
        final WorkflowTemplateModel workflowTemplate = getWorkflowModelFinder().findWorkflowTemplate(context);
        final List<WorkflowActionTemplateModel> actions = MutableListUtil.toMutableList(workflowTemplate.getActions());
        for(final WorkflowActionTemplateModel action : workflowTemplate.getActions())
        {
            if(getNetworkEntityFinder().findActionNode(context, action).isEmpty())
            {
                detachDecisions(action);
                actions.remove(action);
            }
        }
        workflowTemplate.setActions(actions);
    }


    /**
     * Removes the link between action and decision so only the action is being removed.
     *
     * @param action
     */
    protected void detachDecisions(final WorkflowActionTemplateModel action)
    {
        action.getDecisionTemplates().forEach(decision -> decision.setActionTemplate(null));
        action.setDecisionTemplates(Collections.emptySet());
    }


    public NetworkEntityFinder getNetworkEntityFinder()
    {
        return networkEntityFinder;
    }


    @Required
    public void setNetworkEntityFinder(final NetworkEntityFinder networkEntityFinder)
    {
        this.networkEntityFinder = networkEntityFinder;
    }


    public WorkflowModelFinder getWorkflowModelFinder()
    {
        return workflowModelFinder;
    }


    @Required
    public void setWorkflowModelFinder(final WorkflowModelFinder workflowModelFinder)
    {
        this.workflowModelFinder = workflowModelFinder;
    }
}
