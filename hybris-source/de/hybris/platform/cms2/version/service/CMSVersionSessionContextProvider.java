package de.hybris.platform.cms2.version.service;

import de.hybris.platform.cms2.data.CMSItemData;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface CMSVersionSessionContextProvider
{
    default void initCache()
    {
    }


    void addGeneratedItemToCache(ItemModel paramItemModel, CMSVersionModel paramCMSVersionModel);


    Map<CMSVersionModel, ItemModel> getAllGeneratedItemsFromCached();


    void removeGeneratedItemFromCache(CMSVersionModel paramCMSVersionModel);


    void removeAllGeneratedItemsFromCache();


    void addContentSlotForPageToCache(ContentSlotForPageModel paramContentSlotForPageModel);


    List<ContentSlotForPageModel> getAllCachedContentSlotsForPage();


    void removeContentSlotForPageFromCache(ContentSlotForPageModel paramContentSlotForPageModel);


    void removeAllContentSlotsForPageFromCache();


    void addUnsavedVersionedItemToCache(CMSVersionModel paramCMSVersionModel);


    Set<CMSItemData> getAllUnsavedVersionedItemsFromCached();


    void removeAllUnsavedVersionedItemsFromCache();


    boolean isPageVersionedInTransactionCached();


    void addPageVersionedInTransactionToCache(Optional<AbstractPageModel> paramOptional);


    Optional<AbstractPageModel> getPageVersionedInTransactionFromCache();


    void removePageVersionedInTransactionFromCache();
}
