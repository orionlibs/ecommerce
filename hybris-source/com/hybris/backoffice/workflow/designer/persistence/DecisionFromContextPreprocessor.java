/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.services.NetworkEntityFinder;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.backoffice.workflow.designer.services.WorkflowModelFinder;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Required;

/**
 * Creates data associated with {@link WorkflowDecisionTemplateModel}s, excluding links to actions which is handled by
 * {@link LinkFromContextPreprocessor}.
 */
public class DecisionFromContextPreprocessor implements NetworkChartContextPreprocessor
{
    private NetworkEntityFinder networkEntityFinder;
    private WorkflowModelFinder workflowModelFinder;


    /**
     * Creates data related to decisions given in the context.
     *
     * @param context
     *           contains the Workflow Template data of the WorkflowDesigner widget
     */
    @Override
    public void preprocess(final NetworkChartContext context)
    {
        handleRemovedOutgoingDecisions(context);
        handleRemovedIncomingDecisions(context);
        handleNewOrUpdatedDecisions(context);
    }


    /**
     * Handles updates of position on existing decisions or initialization of newly created decisions
     */
    protected void handleNewOrUpdatedDecisions(final NetworkChartContext context)
    {
        for(final Node node : getNetworkEntityFinder().findDecisionNodes(context))
        {
            getWorkflowModelFinder().findWorkflowDecisionInWorkflowTemplateModel(context, node)
                            .ifPresentOrElse(decision -> updateDecisionPosition(node, decision), () -> getWorkflowModelFinder()
                                            .findNewWorkflowDecision(context, node).ifPresent(newDecision -> handleNewDecision(node, newDecision)));
        }
    }


    protected void updateDecisionPosition(final Node node, final WorkflowDecisionTemplateModel decision)
    {
        decision.setVisualisationX(node.getX());
        decision.setVisualisationY(node.getY());
    }


    protected void handleNewDecision(final Node node, final WorkflowDecisionTemplateModel newDecision)
    {
        updateDecisionPosition(node, newDecision);
        newDecision.setToTemplateActions(Collections.emptySet());
    }


    /**
     * Removes outgoing decisions from actions. A decision is considered removed when the node corresponding to model is not
     * present on UI.
     *
     * @see NodeTypeService#isSameDecision(WorkflowDecisionTemplateModel, Node)
     */
    protected void handleRemovedOutgoingDecisions(final NetworkChartContext context)
    {
        final WorkflowTemplateModel workflowTemplate = getWorkflowModelFinder().findWorkflowTemplate(context);
        for(final WorkflowActionTemplateModel action : workflowTemplate.getActions())
        {
            final Collection<WorkflowDecisionTemplateModel> decisions = MutableListUtil.toMutableList(action.getDecisionTemplates());
            for(final WorkflowDecisionTemplateModel decision : action.getDecisionTemplates())
            {
                if(getNetworkEntityFinder().findDecisionNode(context, decision).isEmpty())
                {
                    decisions.remove(decision);
                }
            }
            action.setDecisionTemplates(decisions);
        }
    }


    /**
     * Removes incoming decisions from actions. A decision is considered removed when the node corresponding to model is not
     * present on UI.
     *
     * @see NodeTypeService#isSameDecision(WorkflowDecisionTemplateModel, Node)
     */
    protected void handleRemovedIncomingDecisions(final NetworkChartContext context)
    {
        final WorkflowTemplateModel workflowTemplate = getWorkflowModelFinder().findWorkflowTemplate(context);
        for(final WorkflowActionTemplateModel action : workflowTemplate.getActions())
        {
            final Collection<WorkflowDecisionTemplateModel> decisions = MutableListUtil
                            .toMutableList(action.getIncomingTemplateDecisions());
            for(final WorkflowDecisionTemplateModel decision : action.getIncomingTemplateDecisions())
            {
                if(getNetworkEntityFinder().findDecisionNode(context, decision).isEmpty())
                {
                    decisions.remove(decision);
                }
            }
            action.setIncomingTemplateDecisions(decisions);
        }
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
