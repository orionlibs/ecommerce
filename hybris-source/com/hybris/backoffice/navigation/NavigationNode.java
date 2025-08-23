/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.navigation;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hybris.backoffice.actionbar.ActionDefinition;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.tree.util.TreeUtils;
import java.util.List;

/**
 * Definition of explorer tree node. The typical implementation could be a simple POJO.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface NavigationNode extends ActionDefinition
{
    /**
     * Returns data associated with the node. If no data are associated with the node it returns <code>null</code>.
     *
     * @return data associated with the node or <code>null</code> if no data are associated with the node
     */
    Object getData();


    /**
     * Returns the parent node of this node.
     *
     * @return parent node of this node or <code>null</code> if this is a root node
     */
    NavigationNode getParent();


    /**
     * Sets the parent of this node to the given node.
     *
     * @param parent
     *           a node to become a parent of this node
     */
    void setParent(NavigationNode parent);


    /**
     * Returns all children nodes.
     *
     * @return all children nodes
     */
    List<NavigationNode> getChildren();


    /**
     * Sets the given list to be child nodes of this node.
     *
     * @param children
     *           list of nodes to become children of this node
     */
    void setChildren(List<NavigationNode> children);


    /**
     * Adds the given node as a child of this node.
     *
     * @param child
     *           a node to become a child of this node
     */
    void addChild(NavigationNode child);


    /**
     * Returns the level of this node. A root node has level = 0.
     *
     * @return level of this node
     */
    int getLevel();


    /**
     * Returns true if this node can have children.
     *
     * @return true if this node can have children and false otherwise
     */
    boolean isDirectory();


    /**
     * Returns true if this node should be expanded by default when the tree is created.
     *
     * @return true if this node should be expanded by default; false otherwise
     */
    boolean isExpandedByDefault();


    /**
     * Returns node context
     *
     * @return new DefaultCockpitContext by default
     */
    default CockpitContext getContext()
    {
        return new DefaultCockpitContext();
    }


    /**
     * Sets node context
     *
     * @param cockpitContext
     */
    default void setContext(final CockpitContext cockpitContext)
    {
    }


    default List<NavigationNode> getChildren(final String filterString, final boolean camelCase,
                    final TreeUtils.MatchMode matchMode)
    {
        return getChildren();
    }
}
