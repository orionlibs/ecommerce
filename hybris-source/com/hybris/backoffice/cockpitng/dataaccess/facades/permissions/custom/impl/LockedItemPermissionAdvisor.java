/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.permissions.custom.impl;

import com.hybris.backoffice.cockpitng.dataaccess.facades.permissions.custom.InstancePermissionAdvisor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.locking.ItemLockingService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

/**
 * PermissionAdvisor responsible for evaluating whether it's possible to modify or delete an instance.
 * Implementation is based on {#ItemLockingService}.
 */
public class LockedItemPermissionAdvisor implements InstancePermissionAdvisor<ItemModel>
{
    private ItemLockingService itemLockingService;
    private ModelService modelService;


    @Override
    public boolean canModify(final ItemModel instance)
    {
        return !getItemLockingService().isLocked(instance);
    }


    @Override
    public boolean canDelete(final ItemModel instance)
    {
        return !getItemLockingService().isLocked(instance);
    }


    @Override
    public boolean isApplicableTo(final Object instance)
    {
        return instance instanceof ItemModel && !modelService.isRemoved(instance);
    }


    public ItemLockingService getItemLockingService()
    {
        return itemLockingService;
    }


    @Required
    public void setItemLockingService(final ItemLockingService itemLockingService)
    {
        this.itemLockingService = itemLockingService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return modelService;
    }
}
