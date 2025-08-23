/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer;

import com.hybris.backoffice.workflow.designer.pojo.Workflow;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowAction;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowDecision;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowLink;
import com.hybris.backoffice.workflow.designer.renderer.NetworkEntityRenderer;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default NetworkChart widget elements factory
 */
public class DefaultWorkflowNetworkEntitiesFactory implements WorkflowNetworkEntitiesFactory
{
    private Collection<NetworkEntityRenderer> networkEntityRenderers = Collections.emptySet();


    @Override
    public Network generateNetwork(final Workflow workflow)
    {
        final Map<WorkflowEntity, Node> nodesMap = createItemModelToNodesMap(workflow);
        final Collection<Edge> edges = renderEdges(workflow, nodesMap);
        return new Network(new LinkedHashSet<>(nodesMap.values()), edges);
    }


    /**
     * Creates a map containing item models and their visual representation
     *
     * @param workflow
     *           workflow template that contains items for visual representation
     * @return map items (models) to nodes (visual representation)
     */
    protected Map<WorkflowEntity, Node> createItemModelToNodesMap(final Workflow workflow)
    {
        final Map<WorkflowEntity, Node> nodes = new LinkedHashMap<>();
        for(final WorkflowAction action : workflow.getActions())
        {
            generateNode(action).ifPresent(node -> nodes.put(action, node));
            final Collection<WorkflowDecision> decisions = CollectionUtils.union(action.getDecisions(),
                            action.getIncomingDecisions());
            for(final WorkflowDecision decision : decisions)
            {
                generateNode(decision).ifPresent(node -> nodes.put(decision, node));
            }
            for(final WorkflowLink incomingLink : action.getIncomingLinks())
            {
                generateOrReuseAndNode(nodes, incomingLink);
            }
        }
        return nodes;
    }


    protected void generateOrReuseAndNode(final Map<WorkflowEntity, Node> nodes, final WorkflowLink incomingLink)
    {
        final Consumer<WorkflowLink> reuseAndNodeStrategy = existingLink -> {
            final Node existingAndNode = nodes.get(existingLink);
            nodes.put(incomingLink, existingAndNode);
        };
        final Runnable generateAndNodeStrategy = () -> {
            final Optional<Node> generatedAndNode = generateNode(incomingLink);
            generatedAndNode.ifPresent(node -> nodes.put(incomingLink, node));
        };
        getLinkPointingToSameTarget(nodes, incomingLink).ifPresentOrElse(reuseAndNodeStrategy, generateAndNodeStrategy);
    }


    protected Optional<WorkflowLink> getLinkPointingToSameTarget(final Map<WorkflowEntity, Node> nodes,
                    final WorkflowLink incomingLink)
    {
        final Predicate<WorkflowLink> isLinkPointingToSameTargetPredicate = existingLink -> Objects
                        .equals(existingLink.getModel().getTarget(), incomingLink.getModel().getTarget());
        return nodes.keySet().stream().filter(WorkflowLink.class::isInstance).map(WorkflowLink.class::cast)
                        .filter(isLinkPointingToSameTargetPredicate).findFirst();
    }


    @Override
    public Optional<Node> generateNode(final WorkflowEntity workflowEntity)
    {
        return networkEntityRenderers.stream().filter(renderer -> renderer.canHandle(workflowEntity)).findFirst()
                        .map(renderer -> renderer.render(workflowEntity));
    }


    @Override
    public Optional<Node> generateNode(final WorkflowEntity item, final Node node)
    {
        if(node == null)
        {
            return generateNode(item);
        }
        return networkEntityRenderers.stream().filter(renderer -> renderer.canHandle(item)).findFirst()
                        .map(renderer -> renderer.render(item, node));
    }


    /**
     * Creates a list of edges from connections between models
     *
     * @param workflow
     *           workflow template that contains items for visual representation
     * @param itemModelToNodesMap
     *           a map containing item models and their visual representation
     * @return edges that represent connections in given workflow template
     */
    protected List<Edge> renderEdges(final Workflow workflow, final Map<WorkflowEntity, Node> itemModelToNodesMap)
    {
        final List<Edge> edges = new LinkedList<>();
        for(final WorkflowAction fromAction : workflow.getActions())
        {
            for(final WorkflowDecision decision : fromAction.getDecisions())
            {
                edges.add(new Edge.Builder(itemModelToNodesMap.get(fromAction), itemModelToNodesMap.get(decision)).build());
            }
            for(final WorkflowLink link : fromAction.getIncomingLinks())
            {
                if(itemModelToNodesMap.containsKey(link))
                {
                    edges.add(new Edge.Builder(itemModelToNodesMap.get(link.getSource()), itemModelToNodesMap.get(link)).build());
                    edges.add(new Edge.Builder(itemModelToNodesMap.get(link), itemModelToNodesMap.get(link.getTarget())).build());
                }
                else
                {
                    edges.add(new Edge.Builder(itemModelToNodesMap.get(link.getSource()), itemModelToNodesMap.get(link.getTarget()))
                                    .build());
                }
            }
        }
        return edges;
    }


    @Required
    public void setNetworkEntityRenderers(final Collection<NetworkEntityRenderer> networkEntityRenderers)
    {
        this.networkEntityRenderers = networkEntityRenderers;
    }
}
