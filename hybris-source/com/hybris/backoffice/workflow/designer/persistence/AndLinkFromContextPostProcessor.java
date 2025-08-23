/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.handler.DefaultWorkflowDesignerSaveHandler;
import com.hybris.backoffice.workflow.designer.renderer.AndRenderer;
import com.hybris.backoffice.workflow.designer.services.ConnectionFinder;
import com.hybris.backoffice.workflow.designer.services.NetworkEntityFinder;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.backoffice.workflow.designer.services.WorkflowModelFinder;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Persists links from/to 'and' node.
 */
public class AndLinkFromContextPostProcessor implements NetworkChartContextPostprocessor
{
    private WorkflowTemplateService workflowTemplateService;
    private NodeTypeService nodeTypeService;
    private ModelService modelService;
    private LinkCreator linkCreator;
    private ConnectionFinder connectionFinder;
    private NetworkEntityFinder networkEntityFinder;
    private WorkflowModelFinder workflowModelFinder;


    @Override
    public void postprocess(final NetworkChartContext context)
    {
        handleReplacedAndNodesWithOr(context);
        handleNewAndConnections(context);
    }


    private void handleNewAndConnections(final NetworkChartContext context)
    {
        final Collection<Node> andNodes = getNetworkEntityFinder().findAndNodes(context);
        for(final Node andNode : andNodes)
        {
            final Set<Edge> decisionsToAnd = getNetworkEntityFinder().findEdges(context).stream()
                            .filter(edge -> Objects.equals(edge.getTo(), andNode.getId())).collect(Collectors.toSet());
            final Set<Edge> andToActions = getNetworkEntityFinder().findEdges(context).stream()
                            .filter(edge -> Objects.equals(edge.getFrom(), andNode.getId())).collect(Collectors.toSet());
            checkEachEdgeWithEachEdge(context, decisionsToAnd, andToActions);
        }
    }


    private void checkEachEdgeWithEachEdge(final NetworkChartContext context, final Set<Edge> decisionsToAnd,
                    final Set<Edge> andToActions)
    {
        for(final Edge andToAction : andToActions)
        {
            final Optional<WorkflowActionTemplateModel> workflowAction = findWorkflowActionByCode(context, andToAction.getToNode());
            for(final Edge decisionToAnd : decisionsToAnd)
            {
                final Optional<WorkflowDecisionTemplateModel> workflowDecision = findWorkflowDecisionByCode(context,
                                decisionToAnd.getFromNode());
                if(workflowAction.isPresent() && workflowDecision.isPresent())
                {
                    getLinkCreator().createLinkFromDecisionToAction(workflowDecision.get(), workflowAction.get());
                    getModelService().saveAll(workflowDecision.get(), workflowAction.get());
                    getWorkflowTemplateService().setAndConnectionBetweenActionAndDecision(workflowDecision.get(),
                                    workflowAction.get());
                    workflowAction.get().getIncomingLinkTemplates().stream().filter(this::isAndConnectionTemplate)
                                    .forEach(linkModel -> updateLinkCoordinatesFromAndNode(andToAction.getFromNode(), linkModel));
                }
            }
        }
    }


    private Optional<WorkflowActionTemplateModel> findWorkflowActionByCode(final NetworkChartContext context, final Node node)
    {
        return getWorkflowModelFinder().findAllWorkflowActions(context).stream()
                        .filter(action -> getNodeTypeService().hasCode(node, action.getCode())).findFirst();
    }


    private Optional<WorkflowDecisionTemplateModel> findWorkflowDecisionByCode(final NetworkChartContext context, final Node node)
    {
        final Set<WorkflowDecisionTemplateModel> allWorkflowDecisions = getWorkflowModelFinder().findAllWorkflowDecisions(context);
        final Set<WorkflowDecisionTemplateModel> attribute = (Set<WorkflowDecisionTemplateModel>)context.getWim().getModel()
                        .getValue(DefaultWorkflowDesignerSaveHandler.EXISTING_DECISIONS, Set.class);
        return SetUtils.union(allWorkflowDecisions, attribute).stream()
                        .filter(decision -> getNodeTypeService().hasCode(node, decision.getCode())).findFirst();
    }


    private void updateLinkCoordinatesFromAndNode(final Node andNode, final LinkModel linkModel)
    {
        setAttributeForLink(linkModel, AndRenderer.VISUALISATION_X, andNode.getX());
        setAttributeForLink(linkModel, AndRenderer.VISUALISATION_Y, andNode.getY());
    }


    private void setAttributeForLink(final LinkModel link, final String attribute, final Object value)
    {
        final Link linkSource = getModelService().getSource(link);
        try
        {
            linkSource.setAttribute(attribute, value);
        }
        catch(final JaloBusinessException e)
        {
            throw new WorkflowDesignerSavingException(e);
        }
    }


    private boolean isAndConnectionTemplate(final LinkModel linkModel)
    {
        return LinkAttributeAccessor.getAndConnectionAttribute(linkModel);
    }


    private void handleReplacedAndNodesWithOr(final NetworkChartContext context)
    {
        getReplacedAndLinksWithOrStream(context).forEach(changedLink -> {
            final WorkflowDecisionTemplateModel decision = (WorkflowDecisionTemplateModel)changedLink.getSource();
            final WorkflowActionTemplateModel action = (WorkflowActionTemplateModel)changedLink.getTarget();
            getWorkflowTemplateService().setOrConnectionBetweenActionAndDecision(decision, action);
        });
    }


    private Stream<LinkModel> getReplacedAndLinksWithOrStream(final NetworkChartContext context)
    {
        return getWorkflowModelFinder().findWorkflowActionsFromWorkflowTemplateModel(context).stream()
                        .flatMap(action -> action.getIncomingLinkTemplates().stream()).filter(this::isAndConnectionTemplate)
                        .filter(link -> isAndConnectionPresent(context, link));
    }


    private boolean isAndConnectionPresent(final NetworkChartContext context, final LinkModel link)
    {
        final WorkflowDecisionTemplateModel decision = (WorkflowDecisionTemplateModel)link.getSource();
        final WorkflowActionTemplateModel action = (WorkflowActionTemplateModel)link.getTarget();
        return !getConnectionFinder().isDecisionConnectedToActionThroughAnd(context, decision, action);
    }


    public WorkflowTemplateService getWorkflowTemplateService()
    {
        return workflowTemplateService;
    }


    @Required
    public void setWorkflowTemplateService(final WorkflowTemplateService workflowTemplateService)
    {
        this.workflowTemplateService = workflowTemplateService;
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


    public ModelService getModelService()
    {
        return modelService;
    }


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
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


    public ConnectionFinder getConnectionFinder()
    {
        return connectionFinder;
    }


    @Required
    public void setConnectionFinder(final ConnectionFinder connectionFinder)
    {
        this.connectionFinder = connectionFinder;
    }
}
