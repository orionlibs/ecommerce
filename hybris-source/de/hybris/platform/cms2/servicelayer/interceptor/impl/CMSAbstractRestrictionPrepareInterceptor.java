package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.relateditems.RelatedItemsService;
import de.hybris.platform.cms2.servicelayer.interceptor.service.ItemModelPrepareInterceptorService;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class CMSAbstractRestrictionPrepareInterceptor implements PrepareInterceptor<AbstractRestrictionModel>
{
    private final ModelService modelService;
    private final ItemModelPrepareInterceptorService itemModelPrepareInterceptorService;
    private final CMSRestrictionService cmsRestrictionService;
    private RelatedItemsService relatedItemsService;
    private Predicate<ItemModel> pageTypePredicate;


    public CMSAbstractRestrictionPrepareInterceptor(ModelService modelService, ItemModelPrepareInterceptorService itemModelPrepareInterceptorService, CMSRestrictionService cmsRestrictionService)
    {
        this.modelService = modelService;
        this.itemModelPrepareInterceptorService = itemModelPrepareInterceptorService;
        this.cmsRestrictionService = cmsRestrictionService;
    }


    public void onPrepare(AbstractRestrictionModel restrictionModel, InterceptorContext interceptorContext) throws InterceptorException
    {
        if(getItemModelPrepareInterceptorService().isEnabled() &&
                        !getItemModelPrepareInterceptorService().isFromActiveCatalogVersion((ItemModel)restrictionModel) &&
                        !getItemModelPrepareInterceptorService().isOnlyChangeSynchronizationBlocked((ItemModel)restrictionModel, interceptorContext))
        {
            updateSynchronizationBlockedOnRestriction(restrictionModel);
        }
    }


    private void updateSynchronizationBlockedOnRestriction(AbstractRestrictionModel restrictionModel)
    {
        if(getCmsRestrictionService().relatedSharedSlots(restrictionModel))
        {
            restrictionModel.setSynchronizationBlocked(false);
        }
        else
        {
            List<CMSItemModel> relatedItems = getRelatedItemsService().getRelatedItems((ItemModel)restrictionModel);
            boolean relatedToAnyPage = relatedItems.stream().filter(item -> Objects.nonNull(item.getPk())).anyMatch(getPageTypePredicate());
            restrictionModel.setSynchronizationBlocked(relatedToAnyPage);
        }
    }


    protected ItemModelPrepareInterceptorService getItemModelPrepareInterceptorService()
    {
        return this.itemModelPrepareInterceptorService;
    }


    protected CMSRestrictionService getCmsRestrictionService()
    {
        return this.cmsRestrictionService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
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
