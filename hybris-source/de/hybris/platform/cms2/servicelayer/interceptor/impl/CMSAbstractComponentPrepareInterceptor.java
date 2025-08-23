package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.relateditems.RelatedItemsService;
import de.hybris.platform.cms2.servicelayer.interceptor.service.ItemModelPrepareInterceptorService;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class CMSAbstractComponentPrepareInterceptor implements PrepareInterceptor<AbstractCMSComponentModel>
{
    private final ModelService modelService;
    private final ItemModelPrepareInterceptorService itemModelPrepareInterceptorService;
    private final CMSComponentService cmsComponentService;
    private RelatedItemsService relatedItemsService;
    private Predicate<ItemModel> pageTypePredicate;


    public CMSAbstractComponentPrepareInterceptor(ModelService modelService, ItemModelPrepareInterceptorService itemModelPrepareInterceptorService, CMSComponentService cmsComponentService)
    {
        this.modelService = modelService;
        this.itemModelPrepareInterceptorService = itemModelPrepareInterceptorService;
        this.cmsComponentService = cmsComponentService;
    }


    public void onPrepare(AbstractCMSComponentModel componentModel, InterceptorContext interceptorContext) throws InterceptorException
    {
        if(getItemModelPrepareInterceptorService().isEnabled() &&
                        !getItemModelPrepareInterceptorService().isFromActiveCatalogVersion((ItemModel)componentModel) &&
                        !getItemModelPrepareInterceptorService().isOnlyChangeSynchronizationBlocked((ItemModel)componentModel, interceptorContext))
        {
            updateSynchronizationBlockedOnComponent(componentModel);
        }
    }


    private void updateSynchronizationBlockedOnComponent(AbstractCMSComponentModel componentModel)
    {
        if(unsavedComponentAndUnSavedSlots(componentModel))
        {
            return;
        }
        if(getCmsComponentService().inSharedSlots(componentModel))
        {
            componentModel.setSynchronizationBlocked(false);
        }
        else
        {
            List<CMSItemModel> relatedItems = getRelatedItemsService().getRelatedItems((ItemModel)componentModel);
            boolean relatedToAnyPage = relatedItems.stream().filter(item -> Objects.nonNull(item.getPk())).anyMatch(getPageTypePredicate());
            componentModel.setSynchronizationBlocked(relatedToAnyPage);
        }
    }


    private boolean unsavedComponentAndUnSavedSlots(AbstractCMSComponentModel componentModel)
    {
        if(getModelService().isNew(componentModel))
        {
            return
                            !CollectionUtils.emptyIfNull(componentModel.getSlots()).stream().anyMatch(slot -> !getModelService().isNew(slot));
        }
        return false;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected ItemModelPrepareInterceptorService getItemModelPrepareInterceptorService()
    {
        return this.itemModelPrepareInterceptorService;
    }


    protected CMSComponentService getCmsComponentService()
    {
        return this.cmsComponentService;
    }


    protected RelatedItemsService getRelatedItemsService()
    {
        return this.relatedItemsService;
    }


    protected Predicate<ItemModel> getPageTypePredicate()
    {
        return this.pageTypePredicate;
    }


    @Required
    public void setRelatedItemsService(RelatedItemsService relatedItemsService)
    {
        this.relatedItemsService = relatedItemsService;
    }


    @Required
    public void setPageTypePredicate(Predicate<ItemModel> pageTypePredicate)
    {
        this.pageTypePredicate = pageTypePredicate;
    }
}
