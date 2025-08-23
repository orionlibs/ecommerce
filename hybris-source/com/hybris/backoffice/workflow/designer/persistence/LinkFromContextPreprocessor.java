/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.services.ConnectionFinder;
import com.hybris.backoffice.workflow.designer.services.WorkflowModelFinder;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

/**
 * Creates links from decision to action. Also removes no longer existing links.
 */
public class LinkFromContextPreprocessor implements NetworkChartContextPreprocessor
{
    private ConnectionFinder connectionFinder;
    private LinkCreator linkCreator;
    private WorkflowModelFinder workflowModelFinder;


    @Override
    public void preprocess(final NetworkChartContext context)
    {
        handleNewAndUpdatedDecisionToActionLinks(context);
        handleNewAndUpdatedActionToDecisionLinks(context);
        handleRemovedDecisionToActionLinks(context);
    }


    protected void handleRemovedDecisionToActionLinks(final NetworkChartContext context)
    {
        final WorkflowTemplateModel workflowTemplate = getWorkflowModelFinder().findWorkflowTemplate(context);
        for(final WorkflowActionTemplateModel action : workflowTemplate.getActions())
        {
            for(final WorkflowDecisionTemplateModel incomingDecision : action.getIncomingTemplateDecisions())
            {
                if(getConnectionFinder().findDecisionToActionEdge(context, incomingDecision, action).isEmpty()
                                && !getConnectionFinder().isDecisionConnectedToActionThroughAnd(context, incomingDecision, action))
                {
                    removeDecisionToActionLink(incomingDecision, action);
                }
            }
            for(final WorkflowDecisionTemplateModel decision : action.getDecisionTemplates())
            {
                if(getConnectionFinder().findActionToDecisionEdge(context, action, decision).isEmpty())
                {
                    removeActionToDecisionLink(action, decision);
                }
            }
        }
    }


    protected void removeDecisionToActionLink(final WorkflowDecisionTemplateModel decision,
                    final WorkflowActionTemplateModel action)
    {
        if(action.getIncomingTemplateDecisions().contains(decision))
        {
            final Set<WorkflowActionTemplateModel> toActions = new HashSet<>(decision.getToTemplateActions());
            toActions.remove(action);
            decision.setToTemplateActions(toActions);
            final Set<WorkflowDecisionTemplateModel> incomingTemplateDecisions = new HashSet<>(
                            action.getIncomingTemplateDecisions());
            incomingTemplateDecisions.remove(decision);
            action.setIncomingTemplateDecisions(incomingTemplateDecisions);
        }
    }


    protected void removeActionToDecisionLink(final WorkflowActionTemplateModel action,
                    final WorkflowDecisionTemplateModel decision)
    {
        if(action.getDecisionTemplates().contains(decision))
        {
            if(decision.getActionTemplate().equals(action))
            {
                decision.setActionTemplate(null);
            }
            final Set<WorkflowDecisionTemplateModel> decisionTemplates = new HashSet<>(action.getDecisionTemplates());
            decisionTemplates.remove(decision);
            action.setDecisionTemplates(decisionTemplates);
        }
    }


    protected void handleNewAndUpdatedDecisionToActionLinks(final NetworkChartContext context)
    {
        for(final Edge decisionToAction : getConnectionFinder().findDecisionToActionEdges(context))
        {
            final Optional<WorkflowDecisionTemplateModel> workflowDecision = getWorkflowModelFinder().findWorkflowDecision(context,
                            decisionToAction.getFromNode());
            final Optional<WorkflowActionTemplateModel> workflowAction = getWorkflowModelFinder().findWorkflowAction(context,
                            decisionToAction.getToNode());
            if(workflowDecision.isPresent() && workflowAction.isPresent())
            {
                getLinkCreator().createLinkFromDecisionToAction(workflowDecision.get(), workflowAction.get());
            }
        }
    }


    protected void handleNewAndUpdatedActionToDecisionLinks(final NetworkChartContext context)
    {
        for(final Edge actionToDecision : getConnectionFinder().findActionToDecisionEdges(context))
        {
            final Optional<WorkflowActionTemplateModel> workflowAction = getWorkflowModelFinder().findWorkflowAction(context,
                            actionToDecision.getFromNode());
            final Optional<WorkflowDecisionTemplateModel> workflowDecision = getWorkflowModelFinder().findWorkflowDecision(context,
                            actionToDecision.getToNode());
            if(workflowAction.isPresent() && workflowDecision.isPresent())
            {
                saveLinkFromActionToDecision(workflowAction.get(), workflowDecision.get());
            }
        }
    }


    protected void saveLinkFromActionToDecision(final WorkflowActionTemplateModel workflowAction,
                    final WorkflowDecisionTemplateModel workflowDecision)
    {
        if(!workflowAction.getDecisionTemplates().contains(workflowDecision))
        {
            workflowDecision.setActionTemplate(workflowAction);
            final Collection<WorkflowDecisionTemplateModel> decisionTemplates = MutableListUtil
                            .toMutableList(workflowAction.getDecisionTemplates());
            decisionTemplates.add(workflowDecision);
            workflowAction.setDecisionTemplates(decisionTemplates);
        }
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


    public LinkCreator getLinkCreator()
    {
        return linkCreator;
    }


    @Required
    public void setLinkCreator(final LinkCreator linkCreator)
    {
        this.linkCreator = linkCreator;
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
