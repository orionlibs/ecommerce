/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.synctracker;

import de.hybris.platform.core.model.ItemModel;
import java.util.Set;

/**
 * Handles changes in items or in the items set which are result of the synchronization.
 */
public interface AfterSyncItemsHandler
{
    /**
     * Handles items updated during synchronization
     *
     * @param updatedItems
     *           items updated during synchronization
     * @param sendGlobalEvents
     *           if global events can be sent
     * @return updated items
     */
    Set<ItemModel> handleUpdatedItems(final Set<ItemModel> updatedItems, final boolean sendGlobalEvents);


    /**
     * Handles items deleted during synchronization
     *
     * @param deletedItems
     *           items deleted during synchronization
     * @param sendGlobalEvents
     *           if global events can be sent
     * @return deleted items
     */
    Set<ItemModel> handleDeletedItems(final Set<ItemModel> deletedItems, final boolean sendGlobalEvents);
}
