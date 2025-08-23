package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.interceptor.service.ItemModelPrepareInterceptorService;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections4.CollectionUtils;

public class ContentSlotForPagePrepareInterceptor implements PrepareInterceptor<ContentSlotForPageModel>
{
    private final CMSContentSlotService cmsContentSlotService;
    private final ModelService modelService;
    private final ItemModelPrepareInterceptorService itemModelPrepareInterceptorService;


    public ContentSlotForPagePrepareInterceptor(CMSContentSlotService cmsContentSlotService, ModelService modelService, ItemModelPrepareInterceptorService itemModelPrepareInterceptorService)
    {
        this.cmsContentSlotService = cmsContentSlotService;
        this.modelService = modelService;
        this.itemModelPrepareInterceptorService = itemModelPrepareInterceptorService;
    }


    public void onPrepare(ContentSlotForPageModel slotForPage, InterceptorContext interceptorContext) throws InterceptorException
    {
        if(getItemModelPrepareInterceptorService().isEnabled() &&
                        !getItemModelPrepareInterceptorService().isFromActiveCatalogVersion((ItemModel)slotForPage))
        {
            setSyncFlagForUnsavedModel(slotForPage, interceptorContext);
        }
    }


    private void setSyncFlagForUnsavedModel(ContentSlotForPageModel slotForPage, InterceptorContext ctx)
    {
        ContentSlotModel slot = slotForPage.getContentSlot();
        if(getModelService().isNew(slot))
        {
            slot.setSynchronizationBlocked(true);
            ctx.registerElementFor(slot, PersistenceOperation.SAVE);
            CollectionUtils.emptyIfNull(slot.getCmsComponents())
                            .stream()
                            .filter(componentModel -> getModelService().isNew(componentModel))
                            .forEach(componentModel -> {
                                componentModel.setSynchronizationBlocked(true);
                                ctx.registerElementFor(componentModel, PersistenceOperation.SAVE);
                            });
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
