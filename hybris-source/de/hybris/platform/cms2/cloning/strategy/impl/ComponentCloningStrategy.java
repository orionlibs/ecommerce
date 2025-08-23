package de.hybris.platform.cms2.cloning.strategy.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cloning.service.CMSItemDeepCloningService;
import de.hybris.platform.cms2.cloning.service.CMSModelCloningContextFactory;
import de.hybris.platform.cms2.cloning.service.impl.CMSModelCloningContext;
import de.hybris.platform.cms2.cloning.service.predicate.CMSItemCloneablePredicate;
import de.hybris.platform.cms2.cloning.strategy.CMSCloningStrategy;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class ComponentCloningStrategy implements CMSCloningStrategy<AbstractCMSComponentModel>
{
    private CMSItemDeepCloningService cmsItemDeepCloningService;
    private SessionSearchRestrictionsDisabler cmsSessionSearchRestrictionsDisabler;
    private CMSModelCloningContextFactory cmsModelCloningContextFactory;
    private CatalogVersionService catalogVersionService;
    private CMSItemCloneablePredicate cmsItemCloneablePredicate;


    public AbstractCMSComponentModel clone(AbstractCMSComponentModel sourceComponentModel, Optional<AbstractCMSComponentModel> template, Optional<Map<String, Object>> context) throws CMSItemNotFoundException
    {
        return (AbstractCMSComponentModel)getCmsSessionSearchRestrictionsDisabler().execute(() -> {
            CatalogVersionModel targetCatalogVersionModel = getCatalogVersionService().getSessionCatalogVersions().stream().findFirst().orElse(null);
            Preconditions.checkArgument((targetCatalogVersionModel != null), "CatalogVersion is required to perform this operation, null given");
            Preconditions.checkArgument(getCmsItemCloneablePredicate().test((ItemModel)sourceComponentModel), "Component cannot be cloned. Its type belongs to the nonCloneableTypeList or to the typeBlacklistSet.");
            CMSModelCloningContext cloningContext = getCmsModelCloningContextFactory().createCloningContextWithCatalogVersionPredicates(targetCatalogVersionModel);
            return (AbstractCMSComponentModel)getCmsItemDeepCloningService().deepCloneComponent((ItemModel)sourceComponentModel, (ModelCloningContext)cloningContext);
        });
    }


    public ItemModel cloneForItemModel(ItemModel sourceItemModel) throws CMSItemNotFoundException, IllegalArgumentException
    {
        return (ItemModel)getCmsSessionSearchRestrictionsDisabler().execute(() -> {
            CatalogVersionModel targetCatalogVersionModel = getCatalogVersionService().getSessionCatalogVersions().stream().findFirst().orElse(null);
            Preconditions.checkArgument((targetCatalogVersionModel != null), "CatalogVersion is required to perform this operation, null given");
            Preconditions.checkArgument(getCmsItemCloneablePredicate().test(sourceItemModel), "ItemModel cannot be cloned. Its type belongs to the nonCloneableTypeList or to the typeBlacklistSet.");
            CMSModelCloningContext cloningContext = getCmsModelCloningContextFactory().createCloningContextWithCatalogVersionPredicates(targetCatalogVersionModel);
            return getCmsItemDeepCloningService().deepCloneComponent(sourceItemModel, (ModelCloningContext)cloningContext);
        });
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


    protected SessionSearchRestrictionsDisabler getCmsSessionSearchRestrictionsDisabler()
    {
        return this.cmsSessionSearchRestrictionsDisabler;
    }


    @Required
    public void setCmsSessionSearchRestrictionsDisabler(SessionSearchRestrictionsDisabler cmsSessionSearchRestrictionsDisabler)
    {
        this.cmsSessionSearchRestrictionsDisabler = cmsSessionSearchRestrictionsDisabler;
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


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
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
}
