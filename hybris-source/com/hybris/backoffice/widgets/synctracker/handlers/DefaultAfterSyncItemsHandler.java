/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.synctracker.handlers;

import com.hybris.backoffice.widgets.synctracker.AfterSyncItemsHandler;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import de.hybris.platform.core.model.ItemModel;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAfterSyncItemsHandler implements AfterSyncItemsHandler
{
    private CockpitEventQueue cockpitEventQueue;


    @Override
    public Set<ItemModel> handleUpdatedItems(final Set<ItemModel> updatedItems, final boolean sendGlobalEvents)
    {
        if(!CollectionUtils.isEmpty(updatedItems) && sendGlobalEvents)
        {
            getCockpitEventQueue()
                            .publishEvent(new DefaultCockpitEvent(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, updatedItems, null));
        }
        return updatedItems;
    }


    @Override
    public Set<ItemModel> handleDeletedItems(final Set<ItemModel> deletedItems, final boolean sendGlobalEvents)
    {
        if(!CollectionUtils.isEmpty(deletedItems) && sendGlobalEvents)
        {
            getCockpitEventQueue()
                            .publishEvent(new DefaultCockpitEvent(ObjectCRUDHandler.OBJECTS_DELETED_EVENT, deletedItems, null));
        }
        return deletedItems;
    }


    protected CockpitEventQueue getCockpitEventQueue()
    {
        return cockpitEventQueue;
    }


    @Required
    public void setCockpitEventQueue(final CockpitEventQueue cockpitEventQueue)
    {
        this.cockpitEventQueue = cockpitEventQueue;
    }
}
