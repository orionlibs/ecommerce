package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.jalo.Item;
import java.util.Collection;
import java.util.Map;

public class CatalogItemCopyCreator extends ItemCopyCreator
{
    private ItemSyncTimestamp itemSyncTimestamp;


    public CatalogItemCopyCreator(GenericCatalogCopyContext genericCatalogCopyContext, ItemCopyCreator parent, Item source, Item target, ItemSyncTimestamp itemSyncTimestamp, Collection<String> blackList, Collection<String> whiteList, Map<String, Object> presetedValues)
                    throws IllegalArgumentException
    {
        super(genericCatalogCopyContext, parent, source, target, blackList, whiteList, presetedValues);
        this.itemSyncTimestamp = itemSyncTimestamp;
    }


    public void setSyncTimestamp(ItemSyncTimestamp itemSyncTimestamp)
    {
        this.itemSyncTimestamp = itemSyncTimestamp;
    }


    public ItemSyncTimestamp getSyncTimestamp()
    {
        return this.itemSyncTimestamp;
    }
}
