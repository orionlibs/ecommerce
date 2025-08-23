package de.hybris.platform.cms2.cloning.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cloning.service.CMSItemCloningService;
import de.hybris.platform.cms2.cloning.service.CMSItemDeepCloningService;
import de.hybris.platform.cms2.cloning.service.CMSModelCloningContextFactory;
import de.hybris.platform.cms2.cloning.service.predicate.CMSItemCloneablePredicate;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSItemCloningService implements CMSItemCloningService
{
    private CMSModelCloningContextFactory cmsModelCloningContextFactory;
    private CMSItemCloneablePredicate cmsItemCloneablePredicate;
    private CMSItemDeepCloningService cmsItemDeepCloningService;
    private CMSPageService cmsPageService;


    public void cloneContentSlotComponents(ContentSlotModel sourceContentSlotModel, ContentSlotModel targetContentSlotModel, CatalogVersionModel targetCatalogVersionModel)
    {
        CMSModelCloningContext cloningContext = getCmsModelCloningContextFactory().createCloningContextWithCatalogVersionPredicates(targetCatalogVersionModel);
        List<AbstractCMSComponentModel> components = (List<AbstractCMSComponentModel>)sourceContentSlotModel.getCmsComponents().stream().filter((Predicate)getCmsItemCloneablePredicate()).map(cmsComponent -> {
            AbstractCMSComponentModel clonedCmsComponent = (AbstractCMSComponentModel)getCmsItemDeepCloningService().deepCloneComponent((ItemModel)cmsComponent, (ModelCloningContext)cloningContext);
            clonedCmsComponent.setName(getCmsItemDeepCloningService().generateCloneComponentName(cmsComponent.getName()));
            clonedCmsComponent.setSlots(Collections.singleton(targetContentSlotModel));
            return clonedCmsComponent;
        }).collect(Collectors.toList());
        targetContentSlotModel.setCmsComponents(components);
    }


    public Optional<AbstractCMSComponentModel> cloneComponent(AbstractCMSComponentModel componentModel)
    {
        return cloneComponentInCatalogVersion(componentModel, componentModel.getCatalogVersion());
    }


    protected Optional<AbstractCMSComponentModel> cloneComponentInCatalogVersion(AbstractCMSComponentModel componentModel, CatalogVersionModel targetCatalogVersionModel)
    {
        AbstractCMSComponentModel clonedComponent = null;
        if(getCmsItemCloneablePredicate().test((ItemModel)componentModel))
        {
            CMSModelCloningContext cloningContext = getCmsModelCloningContextFactory().createCloningContextWithCatalogVersionPredicates(targetCatalogVersionModel);
            clonedComponent = (AbstractCMSComponentModel)getCmsItemDeepCloningService().deepCloneComponent((ItemModel)componentModel, (ModelCloningContext)cloningContext);
            clonedComponent.setName(getCmsItemDeepCloningService().generateCloneComponentName(componentModel.getName()));
        }
        return Optional.ofNullable(clonedComponent);
    }


    protected CMSModelCloningContextFactory getCmsModelCloningContextFactory()
    {
        return this.cmsModelCloningContextFactory;
    }


    @Required
    public void setCmsModelCloningContextFactory(CMSModelCloningContextFactory cmsModelCloningContextFactory)
    {
        this.cmsModelCloningContextFactory = cmsModelCloningContextFactory;
    }


    protected CMSItemCloneablePredicate getCmsItemCloneablePredicate()
    {
        return this.cmsItemCloneablePredicate;
    }


    @Required
    public void setCmsItemCloneablePredicate(CMSItemCloneablePredicate cmsItemCloneablePredicate)
    {
        this.cmsItemCloneablePredicate = cmsItemCloneablePredicate;
    }


    protected CMSItemDeepCloningService getCmsItemDeepCloningService()
    {
        return this.cmsItemDeepCloningService;
    }


    @Required
    public void setCmsItemDeepCloningService(CMSItemDeepCloningService cmsItemDeepCloningService)
    {
        this.cmsItemDeepCloningService = cmsItemDeepCloningService;
    }


    protected CMSPageService getCmsPageService()
    {
        return this.cmsPageService;
    }


    @Required
    public void setCmsPageService(CMSPageService cmsPageService)
    {
        this.cmsPageService = cmsPageService;
    }
}
