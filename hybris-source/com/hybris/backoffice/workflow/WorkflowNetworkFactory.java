/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.google.common.collect.ImmutableSet;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowPojoMapper;
import com.hybris.backoffice.workflow.designer.renderer.NetworkEntityRenderer;
import com.hybris.backoffice.workflow.flow.NetworkNodeDecorator;
import com.hybris.backoffice.workflow.flow.WorkflowFlowNetworkFactory;
import com.hybris.backoffice.workflow.flow.WorkflowVisualisationChecker;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Required;

/**
 * Creates the {@link Network} graph to be drawn based on {@link WorkflowActionModel}
 */
public class WorkflowNetworkFactory implements WorkflowFlowNetworkFactory
{
    private static final int ORDER_OF_WORKFLOW_NETWORK_FACTORY = 20000;
    private int order = ORDER_OF_WORKFLOW_NETWORK_FACTORY;
    private WorkflowVisualisationChecker workflowVisualisationChecker;
    private WorkflowItemModelFactory workflowItemModelFactory;
    private Collection<NetworkEntityRenderer> networkEntityRenderers = Collections.emptySet();
    private NetworkNodeDecorator networkNodeDecorator;


    protected static <T> Predicate<T> not(final Predicate<T> predicate)
    {
        return predicate.negate();
    }


    @Override
    public boolean canHandle(final Collection<WorkflowItem> items, final NetworkChartContext context)
    {
        return !getWorkflowVisualisationChecker().isVisualisationSet(context);
    }


    /**
     * Returns the {@link Network} with edges and nodes for later presentation. The {@link WorkflowItem}s are being
     * walked through starting from the ones with isEnd() set to true. Following their neighbors the algorithm favors
     * visiting nodes pointing to already visited ones. That results in nodes being sorted by their neighborhood.
     * Initially with each child of a node the level decreases. Later on the network is balanced vertically so each node
     * is close to its neighbors and the "and" nodes are half level below the merging decisions.
     *
     * @param items
     *           from which the {@link Network} should be built
     * @return Network with edges and nodes sorted by their neighborhood for nicer presentation
     */
    @Override
    public Network create(final Collection<WorkflowItem> items, final NetworkChartContext context)
    {
        final Map<String, WorkflowItem> nodesMap = createNodesMap(items);
        final Deque<String> visitedNodesIds = new ArrayDeque<>();
        final Deque<String> nodesIdsToVisit = new ArrayDeque<>(collectEndActionsIds(items));
        while(!nodesIdsToVisit.isEmpty())
        {
            final String visitingId = nodesIdsToVisit.pollLast();
            final WorkflowItem visiting = nodesMap.get(visitingId);
            visitedNodesIds.push(visitingId);
            visiting.getNeighborsIds() //
                            .stream() //
                            .filter(not(nodesIdsToVisit::contains)) //
                            .filter(not(visitedNodesIds::contains)) //
                            .sorted(byMostNeighborsPointingTo(nodesMap, visitedNodesIds)) //
                            .forEach(unvisitedNeighborId -> nodesMap.computeIfPresent(unvisitedNeighborId, //
                                            (keyIgnored, unvisitedNeighbor) -> {
                                                final int level = isMergingNode(unvisitedNeighbor) ? visiting.getLevel()
                                                                : LevelOffset.above(visiting.getLevel());
                                                unvisitedNeighbor.setLevel(level);
                                                nodesIdsToVisit.addLast(unvisitedNeighborId);
                                                return unvisitedNeighbor;
                                            }));
        }
        balanceNodesLevels(nodesMap);
        final Map<String, Node> nodesInOrder = visitedNodesIds.stream() //
                        .map(nodesMap::get) //
                        .map(workflowItem -> new NodeWorkflowItem(createNode(workflowItem), workflowItem)) //
                        .collect(Collectors.toMap( //
                                        NodeWorkflowItem::getWorkflowItemId, //
                                        NodeWorkflowItem::getDecoratedNode, //
                                        (x, y) -> x, //
                                        LinkedHashMap::new));
        final List<Edge> edges = extractEdges(nodesInOrder, items);
        return new Network(nodesInOrder.values(), edges);
    }


    private Node decorateNode(final Node node, final WorkflowItem workflowItem)
    {
        return networkNodeDecorator.decorate(node, Map.of("level", workflowItem.getLevel(), "id", getId(workflowItem)));
    }


    private Node createNode(final WorkflowItem workflowItem)
    {
        return WorkflowPojoMapper.mapItemToWorkflowEntity(workflowItem.getModel())
                        .flatMap(workflowEntity -> networkEntityRenderers.stream().filter(renderer -> renderer.canHandle(workflowEntity))
                                        .findFirst().map(renderer -> Pair.of(renderer, workflowEntity)))
                        .map(pair -> {
                            final NetworkEntityRenderer renderer = pair.getLeft();
                            final WorkflowEntity workflowEntity = pair.getRight();
                            return renderer.render(workflowEntity);
                        }).orElseGet(workflowItem::createNode);
    }


    private String getId(final WorkflowItem workflowItem)
    {
        return workflowItem instanceof WorkflowItemFromWorkflowLinkModel
                        ? ((WorkflowItemFromWorkflowLinkModel)workflowItem).getAndId() : workflowItem.getId();
    }


    protected Map<String, WorkflowItem> createNodesMap(final Collection<WorkflowItem> items)
    {
        return items.stream()
                        .collect(Collectors.toMap( //
                                        WorkflowItem::getId, Function.identity(), //
                                        workflowItemModelFactory::mergeNeighbors, LinkedHashMap::new));
    }


    protected List<String> collectEndActionsIds(final Collection<WorkflowItem> items)
    {
        return items.stream() //
                        .filter(this::isEndAction) //
                        .map(WorkflowItem::getId).collect(Collectors.toList());
    }


    protected boolean isEndAction(final WorkflowItem item)
    {
        return WorkflowItem.Type.ACTION == item.getType() && item.isEnd();
    }


    protected Comparator<String> byMostNeighborsPointingTo(final Map<String, WorkflowItem> nodes, final Collection<String> targets)
    {
        return (first, second) -> {
            final Collection<String> neighborsOfFirst = nodes.get(first).getNeighborsIds();
            neighborsOfFirst.retainAll(targets);
            final Collection<String> neighborsOfSecond = nodes.get(second).getNeighborsIds();
            neighborsOfSecond.retainAll(targets);
            return neighborsOfFirst.size() - neighborsOfSecond.size();
        };
    }


    protected void balanceNodesLevels(final Map<String, WorkflowItem> nodesMap)
    {
        balanceMergingNodes(nodesMap);
        balanceCallbacks(nodesMap);
    }


    /**
     * Sets the level of merging nodes (e.g. "AND") to half below of its neighbors. The neighbors of the merging nodes
     * are typically decisions. We want to make sure merging nodes and decisions are kept closer than the actions and the
     * decisions.
     *
     * @param nodesMap
     *           a map containing nodes to balance
     */
    protected void balanceMergingNodes(final Map<String, WorkflowItem> nodesMap)
    {
        nodesMap.values().stream() //
                        .filter(this::isMergingNode) //
                        .forEach(mergingNode -> mergingNode.getNeighborsIds().stream() //
                                        .map(nodesMap::get) //
                                        .mapToInt(WorkflowItem::getLevel) //
                                        .average() //
                                        .ifPresent(
                                                        averageLevelOfNeighbors -> mergingNode.setLevel(LevelOffset.halfBelow((int)averageLevelOfNeighbors))));
    }


    protected boolean isMergingNode(final WorkflowItem node)
    {
        return WorkflowItem.Type.AND_LINK.equals(node.getType());
    }


    /**
     * Sets the level of each node to one level above its average neighbors' levels. Neighbors are initially placed one
     * level below its parent which is not desired for the callbacks (decisions that repeat the action that was once
     * completed).
     *
     * @param nodesMap
     *           a map containing nodes to balance
     */
    protected void balanceCallbacks(final Map<String, WorkflowItem> nodesMap)
    {
        nodesMap.forEach((keyIgnored, node) -> node.getNeighborsIds().stream() //
                        .map(nodesMap::get) //
                        .mapToInt(WorkflowItem::getLevel) //
                        .average() //
                        .ifPresent(averageLevelOfNeighbors -> node
                                        .setLevel(Math.max(node.getLevel(), LevelOffset.above((int)averageLevelOfNeighbors)))));
    }


    /**
     * @deprecated use {@link #extractEdges(Map, Collection)} instead
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected List<Edge> extractEdges(final Collection<WorkflowItem> items, final Map<String, WorkflowItem> nodesMap)
    {
        final List<Edge> edges = new LinkedList<>();
        items.forEach(from -> from.getNeighborsIds().stream().map(nodesMap::get) //
                        .forEach(to -> {
                            final Node fromNode = from.createNode();
                            final Node toNode = to.createNode();
                            edges.add(new Edge.Builder(toNode, fromNode) //
                                            .withId(from.getId() + "-" + to.getId()) //
                                            .build());
                        }));
        return edges;
    }


    protected List<Edge> extractEdges(final Map<String, Node> nodesMap, final Collection<WorkflowItem> items)
    {
        final List<Edge> edges = new LinkedList<>();
        items.forEach(from -> from.getNeighborsIds().stream().map(nodesMap::get) //
                        .forEach(to -> {
                            final Node fromNode = nodesMap.get(from.getId());
                            edges.add(new Edge.Builder(to, fromNode) //
                                            .withId(from.getId() + "-" + to.getId()) //
                                            .build());
                        }));
        return edges;
    }


    public WorkflowItemModelFactory getWorkflowItemModelFactory()
    {
        return workflowItemModelFactory;
    }


    @Required
    public void setWorkflowItemModelFactory(final WorkflowItemModelFactory workflowItemModelFactory)
    {
        this.workflowItemModelFactory = workflowItemModelFactory;
    }


    public WorkflowVisualisationChecker getWorkflowVisualisationChecker()
    {
        return workflowVisualisationChecker;
    }


    @Required
    public void setWorkflowVisualisationChecker(final WorkflowVisualisationChecker workflowVisualisationChecker)
    {
        this.workflowVisualisationChecker = workflowVisualisationChecker;
    }


    public Collection<NetworkEntityRenderer> getNetworkEntityRenderers()
    {
        return ImmutableSet.copyOf(networkEntityRenderers);
    }


    @Required
    public void setNetworkEntityRenderers(final Collection<NetworkEntityRenderer> networkEntityRenderers)
    {
        this.networkEntityRenderers = networkEntityRenderers;
    }


    public NetworkNodeDecorator getNetworkNodeDecorator()
    {
        return networkNodeDecorator;
    }


    @Required
    public void setNetworkNodeDecorator(final NetworkNodeDecorator networkNodeDecorator)
    {
        this.networkNodeDecorator = networkNodeDecorator;
    }


    @Override
    public int getOrder()
    {
        return order;
    }


    // optional
    public void setOrder(final int order)
    {
        this.order = order;
    }


    private static class LevelOffset
    {
        private static final int HALF_LEVEL = 1;
        private static final int FULL_LEVEL = 2;


        private LevelOffset()
        {
        }


        static int halfBelow(final int currentLevel)
        {
            return currentLevel + HALF_LEVEL;
        }


        static int above(final int currentLevel)
        {
            return currentLevel - FULL_LEVEL;
        }
    }


    private class NodeWorkflowItem
    {
        private final Node node;
        private final WorkflowItem workflowItem;


        public NodeWorkflowItem(final Node node, final WorkflowItem workflowItem)
        {
            this.node = node;
            this.workflowItem = workflowItem;
        }


        public String getWorkflowItemId()
        {
            return workflowItem.getId();
        }


        public Node getDecoratedNode()
        {
            return decorateNode(node, workflowItem);
        }


        public WorkflowItem getWorkflowItem()
        {
            return workflowItem;
        }


        @Override
        public boolean equals(final Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o == null || getClass() != o.getClass())
            {
                return false;
            }
            final NodeWorkflowItem that = (NodeWorkflowItem)o;
            return Objects.equals(node, that.node) && Objects.equals(workflowItem, that.workflowItem);
        }


        @Override
        public int hashCode()
        {
            return Objects.hash(node, workflowItem);
        }
    }
}
