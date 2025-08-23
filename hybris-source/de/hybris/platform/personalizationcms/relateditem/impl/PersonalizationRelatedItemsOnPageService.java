package de.hybris.platform.personalizationcms.relateditem.impl;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.impl.DefaultRelatedItemsOnPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class PersonalizationRelatedItemsOnPageService extends DefaultRelatedItemsOnPageService
{
    private final Predicate<ItemModel> cxContainerPredicate;


    public PersonalizationRelatedItemsOnPageService(CMSPageService cmsPageService, CMSRestrictionService cmsRestrictionService, CMSComponentService cmsComponentService, Predicate<ItemModel> cxContainerPredicate)
    {
        super(cmsPageService, cmsRestrictionService, cmsComponentService);
        this.cxContainerPredicate = cxContainerPredicate;
    }


    public Set<CMSItemModel> getRelatedItems(AbstractPageModel pageModel)
    {
        Set<CMSItemModel> relatedItems = super.getRelatedItems(pageModel);
        Objects.requireNonNull(CxCmsComponentContainerModel.class);
        Set<AbstractCMSComponentModel> cmsComponentModels = (Set<AbstractCMSComponentModel>)relatedItems.stream().filter(itemModel -> getCxContainerPredicate().test(itemModel)).map(CxCmsComponentContainerModel.class::cast).map(CxCmsComponentContainerModel::getDefaultCmsComponent)
                        .collect(Collectors.toSet());
        relatedItems.addAll(cmsComponentModels);
        relatedItems.addAll(getChildComponentsAndRelatedRestrictions(cmsComponentModels, pageModel.getCatalogVersion()));
        return relatedItems;
    }


    protected Predicate<ItemModel> getCxContainerPredicate()
    {
        return this.cxContainerPredicate;
    }
}
