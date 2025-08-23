/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree.refreshstrategy;

import com.hybris.backoffice.navigation.NavigationNode;
import java.util.Collection;

/**
 * Strategy provides item models to updated in explorer tree widget
 */
public interface ExplorerTreeRefreshStrategy
{
    /**
     * Returns list of related objects which should be refreshed in the explorer tree widget
     *
     * @param updatedObject
     * @return Collection of related objects
     */
    Collection<? extends Object> findRelatedObjectsToRefresh(final Object updatedObject);


    /**
     * Performs a refresh for particular node.
     *
     * @param node
     *           node to be refreshed
     */
    default void refreshNode(final NavigationNode node)
    {
    }
}
