/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model class that contains current state of search process.
 */
public class ContextSearchProgress extends ContextSearchTree
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextSearchProgress.class);
    private final Map<NodeIdentity, ContextSearchNode> nodes = new HashMap<>();
    private final Map<ContextSearchNode, Collection<ContextSearchNode>> paths = new HashMap<>(
                    Collections.singletonMap(this, Collections.emptyList()));
    /**
     * Level on which search is currently performed
     */
    private ContextSearchNode currentNode = this;


    /**
     * Relevance zones for this search
     *
     * @param relevanceZones
     *           relevance zones in order from most important to least important
     */
    public ContextSearchProgress(final Collection<String> relevanceZones)
    {
        super(relevanceZones);
    }


    /**
     * Expands current node with provided search needle.
     * <P>
     * Method checks whether provided child searches were already performed in current tree. If so then relevance level is
     * checked and updated if needed (a node is moved from lower relevance node to current node).
     *
     * @param request
     *           children request
     * @return search nodes that was not executed yet
     */
    public Iterator<ContextSearchNode> expand(final ContextSearchRequest request, final ContextSearchTerms terms,
                    final NodeRelevance relevance)
    {
        LOGGER.debug("Context search node expanded: {}", currentNode);
        return new NodeIterator(request, terms, relevance);
    }


    private void updateParent(final ContextSearchNode node)
    {
        final Collection<ContextSearchNode> currentPath = paths.get(currentNode);
        final Collection<ContextSearchNode> nodePath = new LinkedHashSet<>(currentPath);
        nodePath.add(currentNode);
        paths.put(node, nodePath);
        node.setParent(currentNode);
    }


    private List<ContextSearchNeedle> extractSearchNeedles(final Map<String, List<String>> request)
    {
        final List<Set<ImmutablePair<String, String>>> setList = request.entrySet().stream().map(entry -> entry.getValue().stream()
                                        .map(value -> new ImmutablePair<>(entry.getKey(), value)).collect(Collectors.toCollection(LinkedHashSet::new)))
                        .collect(Collectors.toList());
        final Set<List<ImmutablePair<String, String>>> lists = Sets.cartesianProduct(setList);
        return lists.stream()
                        .map(list -> list.stream()
                                        .collect(Collectors.toMap(ImmutablePair::getKey, ImmutablePair::getValue, (e1, e2) -> e1, LinkedHashMap::new)))
                        .map(ContextSearchNeedle::new).collect(Collectors.toList());
    }


    private boolean isChild(final ContextSearchNode parent, final ContextSearchNode node)
    {
        return paths.containsKey(node) && paths.get(node).contains(parent);
    }


    /**
     * Moves a level up in search tree. A parent node of currently processed is selected as current.
     *
     * @return <code>true</code> if parent node is available and was successfully selected
     */
    public boolean collapse()
    {
        if(currentNode != this)
        {
            currentNode.commit();
            LOGGER.debug("Context search node collapsed: {}", currentNode);
            selectNode(currentNode.getParent());
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * Marks provided search node as one that is currently processed.
     *
     * @param node
     *           node to be selected
     * @throws IllegalArgumentException thrown when provided node is not defined in this tree
     */
    public void selectNode(final ContextSearchNode node)
    {
        ContextSearchNode n = node;
        while(n.getParent() != null)
        {
            n = n.getParent();
        }
        if(n != this)
        {
            throw new IllegalArgumentException("Provided search node does not belong to this search tree: " + node);
        }
        if(node.isCommitted())
        {
            throw new IllegalStateException("Unable to traverse over committed nodes: " + node);
        }
        currentNode = node;
        LOGGER.debug("Context search node selected: {}", node);
    }


    /**
     * Gets search node that is currently processed.
     *
     * @return a node that is currently processed or <code>null</code> if nothing is being processed
     */
    public ContextSearchNode getCurrentNode()
    {
        return currentNode;
    }


    private static class NodeIdentity
    {
        private final ContextSearchRestriction restriction;
        private final ContextSearchTerms terms;
        private final ContextSearchNeedle searchNeedle;


        private NodeIdentity(final ContextSearchRestriction restriction, final ContextSearchTerms terms,
                        final ContextSearchNeedle searchNeedle)
        {
            this.restriction = restriction;
            this.terms = terms;
            this.searchNeedle = searchNeedle;
        }


        @Override
        public boolean equals(final Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o == null)
            {
                return false;
            }
            if(o.getClass() != this.getClass())
            {
                return false;
            }
            final NodeIdentity that = (NodeIdentity)o;
            if(!restriction.equals(that.restriction))
            {
                return false;
            }
            if(!terms.equals(that.terms))
            {
                return false;
            }
            return searchNeedle.equals(that.searchNeedle);
        }


        @Override
        public int hashCode()
        {
            int result = restriction.hashCode();
            result = 31 * result + terms.hashCode();
            result = 31 * result + searchNeedle.hashCode();
            return result;
        }
    }


    public class NodeIterator implements Iterator<ContextSearchNode>
    {
        final ContextSearchRequest request;
        private final ContextSearchTerms terms;
        private final NodeRelevance relevance;
        final Iterator<ContextSearchNeedle> needles;
        NodeIdentity currentIdentity;
        ContextSearchNeedle currentNeedle;


        public NodeIterator(final ContextSearchRequest request, final ContextSearchTerms terms, final NodeRelevance relevance)
        {
            this.request = request;
            this.terms = terms;
            this.relevance = relevance;
            this.needles = extractSearchNeedles(request.getAttributesNeedle()).iterator();
        }


        protected void move()
        {
            while(currentNeedle == null && needles.hasNext())
            {
                final ContextSearchNeedle needle = needles.next();
                final NodeIdentity nodeIdentity = new NodeIdentity(request.getParentRestriction(), terms, needle);
                ContextSearchNode node = nodes.get(nodeIdentity);
                if(node == null)
                {
                    currentNeedle = needle;
                    currentIdentity = nodeIdentity;
                }
                else if(!isChild(node, currentNode))
                {
                    node = new ContextSearchNodeReference(node);
                    updateParent(node);
                }
                else if(LOGGER.isWarnEnabled())
                {
                    LOGGER.warn("Circular dependency detected!");
                    final StringBuilder log = new StringBuilder();
                    ContextSearchNode current = currentNode;
                    final List<ContextSearchNode> path = new ArrayList<>();
                    while(ObjectUtils.notEqual(current, this))
                    {
                        path.add(current);
                        current = current.getParent();
                    }
                    Collections.reverse(path);
                    path.add(node);
                    path.forEach(element -> log.append(element.toString()).append(System.lineSeparator()));
                    LOGGER.warn(log.toString());
                }
            }
        }


        @Override
        public boolean hasNext()
        {
            if(currentNeedle == null)
            {
                move();
            }
            return currentNeedle != null;
        }


        @Override
        public ContextSearchNode next()
        {
            if(currentNeedle == null)
            {
                move();
            }
            if(currentNeedle != null)
            {
                final DefaultContextSearchNode node = new DefaultContextSearchNode(currentNeedle, relevance);
                updateParent(node);
                nodes.put(currentIdentity, node);
                currentNeedle = null;
                currentIdentity = null;
                return node;
            }
            else
            {
                throw new NoSuchElementException("Unable to move outside of search needles set");
            }
        }


        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
}
