package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.interceptor.service.ItemModelPrepareInterceptorService;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

public class CMSContentSlotPrepareInterceptor implements PrepareInterceptor<ContentSlotModel>
{
    private final CMSContentSlotService cmsContentSlotService;
    private final ModelService modelService;
    private final ItemModelPrepareInterceptorService itemModelPrepareInterceptorService;


    public CMSContentSlotPrepareInterceptor(CMSContentSlotService cmsContentSlotService, ModelService modelService, ItemModelPrepareInterceptorService itemModelPrepareInterceptorService)
    {
        this.cmsContentSlotService = cmsContentSlotService;
        this.modelService = modelService;
        this.itemModelPrepareInterceptorService = itemModelPrepareInterceptorService;
    }


    public void onPrepare(ContentSlotModel contentSlotModel, InterceptorContext interceptorContext) throws InterceptorException
    {
        if(getItemModelPrepareInterceptorService().isEnabled() &&
                        !getItemModelPrepareInterceptorService().isFromActiveCatalogVersion((ItemModel)contentSlotModel) &&
                        !getItemModelPrepareInterceptorService().isOnlyChangeSynchronizationBlocked((ItemModel)contentSlotModel, interceptorContext))
        {
            updateSynchronizationBlockedOnContentSlot(contentSlotModel);
        }
    }


    private void updateSynchronizationBlockedOnContentSlot(ContentSlotModel contentSlotModel)
    {
        if(!getModelService().isNew(contentSlotModel))
        {
            contentSlotModel.setSynchronizationBlocked(!getCmsContentSlotService().isSharedSlot(contentSlotModel));
        }
    }


    protected CMSContentSlotService getCmsContentSlotService()
    {
        return this.cmsContentSlotService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected ItemModelPrepareInterceptorService getItemModelPrepareInterceptorService()
    {
        return this.itemModelPrepareInterceptorService;
    }
}
