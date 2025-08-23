package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.relatedpages.service.RelatedPageRejectionService;
import de.hybris.platform.cms2.servicelayer.interceptor.service.ItemModelPrepareInterceptorService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import org.springframework.beans.factory.annotation.Required;

public class RelatedPagePrepareInterceptor implements PrepareInterceptor<ItemModel>
{
    private RelatedPageRejectionService relatedPageRejectionService;
    private ItemModelPrepareInterceptorService itemModelPrepareInterceptorService;


    public void onPrepare(ItemModel itemModel, InterceptorContext interceptorContext) throws InterceptorException
    {
        if(getItemModelPrepareInterceptorService().isEnabled() &&
                        !getItemModelPrepareInterceptorService().isFromActiveCatalogVersion(itemModel) &&
                        !getItemModelPrepareInterceptorService().isOnlyChangeSynchronizationBlocked(itemModel, interceptorContext))
        {
            getRelatedPageRejectionService().rejectAllRelatedPages(itemModel, interceptorContext);
        }
    }


    protected RelatedPageRejectionService getRelatedPageRejectionService()
    {
        return this.relatedPageRejectionService;
    }


    @Required
    public void setRelatedPageRejectionService(RelatedPageRejectionService relatedPageRejectionService)
    {
        this.relatedPageRejectionService = relatedPageRejectionService;
    }


    protected ItemModelPrepareInterceptorService getItemModelPrepareInterceptorService()
    {
        return this.itemModelPrepareInterceptorService;
    }


    @Required
    public void setItemModelPrepareInterceptorService(ItemModelPrepareInterceptorService itemModelPrepareInterceptorService)
    {
        this.itemModelPrepareInterceptorService = itemModelPrepareInterceptorService;
    }
}
