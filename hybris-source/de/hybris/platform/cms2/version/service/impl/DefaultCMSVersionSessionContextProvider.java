package de.hybris.platform.cms2.version.service.impl;

import de.hybris.platform.cms2.common.service.SessionCachedContextProvider;
import de.hybris.platform.cms2.data.CMSItemData;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSVersionSessionContextProvider implements CMSVersionSessionContextProvider
{
    private SessionCachedContextProvider sessionCachedContextProvider;
    private ObjectFactory<CMSItemData> cmsItemDataFactory;


    public void initCache()
    {
        getSessionCachedContextProvider().getAllItemsFromMapCache("cachedItems");
        getSessionCachedContextProvider().getAllItemsFromListCache("cachedContentSlotsForPage");
        getSessionCachedContextProvider().getAllItemsFromSetCache("cachedUnsavedVersionedItems");
        getSessionCachedContextProvider().getAllItemsFromListCache("cachedPageVersionedInTransaction");
    }


    public void addGeneratedItemToCache(ItemModel itemModel, CMSVersionModel versionModel)
    {
        getSessionCachedContextProvider().addItemToMapCache("cachedItems", versionModel, itemModel);
    }


    public Map<CMSVersionModel, ItemModel> getAllGeneratedItemsFromCached()
    {
        return getSessionCachedContextProvider().getAllItemsFromMapCache("cachedItems");
    }


    public void removeGeneratedItemFromCache(CMSVersionModel versionModel)
    {
        getSessionCachedContextProvider().removeItemFromMapCache("cachedItems", versionModel);
    }


    public void removeAllGeneratedItemsFromCache()
    {
        getSessionCachedContextProvider().clearMapCache("cachedItems");
    }


    public void addContentSlotForPageToCache(ContentSlotForPageModel contentSlotForPageModel)
    {
        getSessionCachedContextProvider().addItemToListCache("cachedContentSlotsForPage", contentSlotForPageModel);
    }


    public List<ContentSlotForPageModel> getAllCachedContentSlotsForPage()
    {
        return getSessionCachedContextProvider().getAllItemsFromListCache("cachedContentSlotsForPage");
    }


    public void removeContentSlotForPageFromCache(ContentSlotForPageModel contentSlotForPageModel)
    {
        getSessionCachedContextProvider().removeItemFromListCache("cachedContentSlotsForPage", contentSlotForPageModel);
    }


    public void removeAllContentSlotsForPageFromCache()
    {
        getSessionCachedContextProvider().clearListCache("cachedContentSlotsForPage");
    }


    public void addUnsavedVersionedItemToCache(CMSVersionModel versionModel)
    {
        CMSItemData itemData = (CMSItemData)getCmsItemDataFactory().getObject();
        itemData.setCatalogId(versionModel.getItemCatalogVersion().getCatalog().getId());
        itemData.setCatalogVersion(versionModel.getItemCatalogVersion().getVersion());
        itemData.setUid(versionModel.getItemUid());
        itemData.setPk(versionModel.getPk());
        itemData.setTypeCode(versionModel.getItemTypeCode());
        getSessionCachedContextProvider().addItemToSetCache("cachedUnsavedVersionedItems", itemData);
    }


    public Set<CMSItemData> getAllUnsavedVersionedItemsFromCached()
    {
        return getSessionCachedContextProvider().getAllItemsFromSetCache("cachedUnsavedVersionedItems");
    }


    public void removeAllUnsavedVersionedItemsFromCache()
    {
        getSessionCachedContextProvider().clearSetCache("cachedUnsavedVersionedItems");
    }


    public boolean isPageVersionedInTransactionCached()
    {
        return getSessionCachedContextProvider().hasCacheKey("cachedPageVersionedInTransaction");
    }


    public void addPageVersionedInTransactionToCache(Optional<AbstractPageModel> abstractPageModel)
    {
        if(abstractPageModel.isPresent())
        {
            getSessionCachedContextProvider().addItemToListCache("cachedPageVersionedInTransaction", abstractPageModel.get());
        }
        else
        {
            getSessionCachedContextProvider().createEmptyListCache("cachedPageVersionedInTransaction");
        }
    }


    public Optional<AbstractPageModel> getPageVersionedInTransactionFromCache()
    {
        List<AbstractPageModel> cachedPagesList = getSessionCachedContextProvider().getAllItemsFromListCache("cachedPageVersionedInTransaction");
        return cachedPagesList.stream()
                        .findFirst();
    }


    public void removePageVersionedInTransactionFromCache()
    {
        getSessionCachedContextProvider().clearListCache("cachedPageVersionedInTransaction");
    }


    protected SessionCachedContextProvider getSessionCachedContextProvider()
    {
        return this.sessionCachedContextProvider;
    }


    @Required
    public void setSessionCachedContextProvider(SessionCachedContextProvider sessionCachedContextProvider)
    {
        this.sessionCachedContextProvider = sessionCachedContextProvider;
    }


    protected ObjectFactory<CMSItemData> getCmsItemDataFactory()
    {
        return this.cmsItemDataFactory;
    }


    @Required
    public void setCmsItemDataFactory(ObjectFactory<CMSItemData> cmsItemDataFactory)
    {
        this.cmsItemDataFactory = cmsItemDataFactory;
    }
}
