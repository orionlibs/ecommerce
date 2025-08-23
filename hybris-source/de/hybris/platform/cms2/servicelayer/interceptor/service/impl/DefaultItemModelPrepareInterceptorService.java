package de.hybris.platform.cms2.servicelayer.interceptor.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.interceptor.service.ItemModelPrepareInterceptorService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.Objects;
import java.util.function.Predicate;

public class DefaultItemModelPrepareInterceptorService implements ItemModelPrepareInterceptorService
{
    private boolean isEnabled = true;
    private final Predicate<ItemModel> cmsItemTypePredicate;
    private final Predicate<ItemModel> contentSlotForPageModelPredicate;


    public DefaultItemModelPrepareInterceptorService(Predicate<ItemModel> cmsItemTypePredicate, Predicate<ItemModel> contentSlotForPageModelPredicate)
    {
        this.cmsItemTypePredicate = cmsItemTypePredicate;
        this.contentSlotForPageModelPredicate = contentSlotForPageModelPredicate;
    }


    public boolean isFromActiveCatalogVersion(ItemModel item)
    {
        if(getCmsItemTypePredicate().test(item))
        {
            CatalogVersionModel catalogVersion = ((CMSItemModel)item).getCatalogVersion();
            return isActiveVersion(catalogVersion);
        }
        if(getContentSlotForPageModelPredicate().test(item))
        {
            CatalogVersionModel catalogVersion = ((ContentSlotForPageModel)item).getCatalogVersion();
            return isActiveVersion(catalogVersion);
        }
        return false;
    }


    public boolean isOnlyChangeSynchronizationBlocked(ItemModel itemModel, InterceptorContext interceptorContext)
    {
        return (Objects.nonNull(interceptorContext) && interceptorContext.getDirtyAttributes(itemModel).size() == 1 && interceptorContext
                        .getDirtyAttributes(itemModel).containsKey("synchronizationBlocked"));
    }


    public boolean isEnabled()
    {
        return this.isEnabled;
    }


    public void setEnabled(boolean isEnabled)
    {
        this.isEnabled = isEnabled;
    }


    protected boolean isActiveVersion(CatalogVersionModel itemCatalogVersion)
    {
        return itemCatalogVersion.equals(itemCatalogVersion.getCatalog().getActiveCatalogVersion());
    }


    protected Predicate<ItemModel> getCmsItemTypePredicate()
    {
        return this.cmsItemTypePredicate;
    }


    protected Predicate<ItemModel> getContentSlotForPageModelPredicate()
    {
        return this.contentSlotForPageModelPredicate;
    }
}
