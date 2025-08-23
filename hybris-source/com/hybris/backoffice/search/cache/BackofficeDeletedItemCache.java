package com.hybris.backoffice.search.cache;

import de.hybris.platform.core.model.ItemModel;

public interface BackofficeDeletedItemCache
{
    boolean storeDeletedItem(ItemModel paramItemModel);


    boolean isExistingInCache(ItemModel paramItemModel);
}
