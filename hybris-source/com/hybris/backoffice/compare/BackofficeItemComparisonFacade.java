/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.compare;

import com.hybris.cockpitng.dataaccess.facades.compare.PermissionsAwareItemComparisonFacade;
import de.hybris.platform.core.model.ItemModel;

/**
 * Comparison Facade for Backoffice application. Main different between this and default implementation is to compare
 * item with each other. Compute when they are equal or the same.
 */
public class BackofficeItemComparisonFacade extends PermissionsAwareItemComparisonFacade
{
    @Override
    public boolean isSameItem(final Object object1, final Object object2)
    {
        if(object1 instanceof ItemModel && object2 instanceof ItemModel)
        {
            return isSameItem((ItemModel)object1, (ItemModel)object2);
        }
        return object1 == object2;
    }


    @Override
    public boolean isEqualItem(final Object object1, final Object object2)
    {
        if(object1 instanceof ItemModel && object2 instanceof ItemModel)
        {
            return isEqualItem((ItemModel)object1, (ItemModel)object2);
        }
        return object1 != null && object1.equals(object2);
    }


    protected boolean isSameItem(final ItemModel itemModel1, final ItemModel itemModel2)
    {
        return itemModel1.getPk().equals(itemModel2.getPk());
    }


    protected boolean isEqualItem(final ItemModel itemModel1, final ItemModel itemModel2)
    {
        return isSameItem(itemModel1, itemModel2) && itemModel1.getItemModelContext().getPersistenceVersion() == itemModel2
                        .getItemModelContext().getPersistenceVersion();
    }
}