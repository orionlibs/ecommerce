/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Represents single search request node.
 * <p>
 * Searching for contexts is performed by traversing through context tree in regards to <code>merge-by</code>
 * attributes, obligatory merge attributes and/or appropriate
 * {@link com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy}. Each step down the tree means that any
 * contexts found are a little bit less relevant to actual request.
 * <p>
 * This interface represents such single step of search
 */
public interface ContextSearchNode
{
    /**
     * Gets relevance of this node in regards to original request
     *
     * @return node relevance
     */
    NodeRelevance getRelevance();


    /**
     * Gets highest relevance of all children with any result in regards to original request
     *
     * @return node's relevance if any result is bound to it, highest relevance of children with result or
     *         <code>null</code> if no result is bound to this branch
     */
    NodeRelevance getNotEmptyRelevance();


    /**
     * Return a level on which the node is situated - minimum number of steps needed to come from original request to
     * this.
     *
     * @return <code>0</code> for root level and 1 more for each level of relevance
     */
    int getLevel();


    /**
     * Search request represented by this node
     *
     * @return search request or <code>null</code> if node is only container for other requests
     */
    ContextSearchNeedle getSearchNeedle();


    /**
     * Checks how node is interpreting children results. If a node is egalitarian, then result of all children are
     * considered as a result of this node. Otherwise only a child with highest relevance is taken under consideration.
     *
     * @return <code>true</code> if node is egalitarian
     * @see #getNotEmptyRelevance()
     * @see #getResult()
     */
    boolean isEgalitarian();


    /**
     * Informs a node how it should interpret its children's results. If a node is egalitarian, then result of all
     * children are considered as a result of this node. Otherwise only a child with highest relevance is taken under
     * consideration.
     *
     * @param egalitarian
     *           <code>true</code> if node should be egalitarian
     * @throws IllegalStateException
     *            thrown if node is already committed and should not be changed
     */
    void setEgalitarian(boolean egalitarian) throws IllegalStateException;


    /**
     * Adds new search result to current node.
     *
     * @param result
     *           context that matches node's request
     * @return <code>true</code> if result has not yet been known and changed nodes result set.
     * @throws IllegalStateException
     *            thrown if node is already committed and should not be changed
     */
    boolean addNodeResult(Context result) throws IllegalStateException;


    /**
     * Gets search results bound to this node.
     *
     * @return list of contexts that matches this node's needle
     */
    List<Context> getNodeResult();


    /**
     * Gets search results bound to this node and its children. Result should be ordered by their relevance (from most
     * relevant to least).
     *
     * @return list of contexts that matches all requests in this branch
     * @see #isEgalitarian()
     */
    List<Context> getResult();


    /**
     * Gets parent search node.
     * <P>
     * Parent node is a search needle that caused search represented by this node.
     *
     * @return parent search node or <code>null</code> if it is root search
     */
    ContextSearchNode getParent();


    /**
     * Updates parent search node.
     * <P>
     * Parent node is a search needle that caused search represented by this node.
     *
     * @param parent
     *           parent search node or <code>null</code> if it is root search
     * @throws IllegalStateException
     *            thrown if node is already committed and should not be changed
     */
    void setParent(ContextSearchNode parent) throws IllegalStateException;


    /**
     * Adds new child search node.
     * <P>
     * A child search node is a search that is related to this node and comes from its dependencies.
     *
     * @param child
     *           child node
     * @throws IllegalStateException
     *            thrown if node is already committed and should not be changed
     */
    void addChild(ContextSearchNode child) throws IllegalStateException;


    /**
     * Removes a child search node.
     * <P>
     * A child search node is a search that is related to this node and comes from its dependencies.
     *
     * @param child
     *           child node
     * @throws IllegalStateException
     *            thrown if node is already committed and should not be changed
     */
    void removeChild(ContextSearchNode child) throws IllegalStateException;


    /**
     * Gets all child search nodes.
     * <P>
     * A child search node is a search that is related to this node and comes from its dependencies.
     *
     * @return collection of child nodes
     */
    Collection<ContextSearchNode> getChildren();


    /**
     * Writes XML representation of search node to provided stream
     *
     * @param os
     *           destination stream
     * @throws IOException
     *            thrown, if there were problems during writing to stream
     */
    void toXML(final OutputStream os) throws IOException;


    /**
     * Checks if node is committed.
     * <P>
     * Search node should be committed after all searches bound to this node has been performed, all results has been
     * assigned and nothing should be changed regarding this node and all its children.
     * <P>
     * Committed node becomes immutable and any action that may change it's result will result in
     * {@link IllegalStateException}.
     *
     * @return <code>true</code> if search bound to this node has been performed and finished
     */
    boolean isCommitted();


    /**
     * Marks node as committed.
     * <P>
     * Search node should be committed after all searches bound to this node has been performed, all results has been
     * assigned and nothing should be changed regarding this node and all its children.
     * <P>
     * Committed node becomes immutable and any action that may change it's result will result in
     * {@link IllegalStateException}.
     */
    void commit();


    Comparator<ContextSearchNode> INC_RELEVANCE_COMPARATOR = (n1, n2) -> n1.getRelevance().compareTo(n2.getRelevance());
    Comparator<ContextSearchNode> DEC_RELEVANCE_COMPARATOR = (n1, n2) -> n2.getRelevance().compareTo(n1.getRelevance());
}