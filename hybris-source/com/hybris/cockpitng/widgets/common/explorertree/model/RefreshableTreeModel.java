/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree.model;

import java.util.List;

/**
 * Mutable tree model
 *
 * @param <E>
 *           type of model elements
 */
public interface RefreshableTreeModel<E>
{
    /**
     * Sets new children nodes of specified parent node and refreshes all data related to them.
     *
     * @param node
     *           parent node
     * @param children
     *           new children nodes
     */
    void refreshChildren(final E node, final List<E> children);


    /**
     * Sets new data cache of specified node.
     *
     * @param node
     *           specified node
     */
    default void refreshNodeCache(final E node)
    {
        //Do nothing
    }


    /**
     * Gets all nodes related to specified data.
     * <P>
     * In case of dynamic trees, method should return only those nodes that are already known and avoid traversing through
     * whole tree to gather all related nodes.
     * </P>
     *
     * @param data
     *           node data
     * @return list of known nodes that are related to data
     */
    List<E> findNodesByData(final Object data);
}
