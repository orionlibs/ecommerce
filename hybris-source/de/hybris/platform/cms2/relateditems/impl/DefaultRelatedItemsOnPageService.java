package de.hybris.platform.cms2.relateditems.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.RelatedItemsOnPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public class DefaultRelatedItemsOnPageService implements RelatedItemsOnPageService
{
    private final CMSPageService cmsPageService;
    private final CMSRestrictionService cmsRestrictionService;
    private final CMSComponentService cmsComponentService;


    public DefaultRelatedItemsOnPageService(CMSPageService cmsPageService, CMSRestrictionService cmsRestrictionService, CMSComponentService cmsComponentService)
    {
        this.cmsPageService = cmsPageService;
        this.cmsRestrictionService = cmsRestrictionService;
        this.cmsComponentService = cmsComponentService;
    }


    public Set<CMSItemModel> getRelatedItems(AbstractPageModel pageModel)
    {
        Set<CMSItemModel> relatedItems = new HashSet<>();
        Set<ContentSlotModel> slots = (Set<ContentSlotModel>)getCmsPageService().getOwnContentSlotsForPage(pageModel).stream().map(contentSlotForPage -> contentSlotForPage.getContentSlot()).filter(Objects::nonNull).collect(Collectors.toSet());
        relatedItems.addAll(slots);
        Set<AbstractCMSComponentModel> cmsComponentModels = (Set<AbstractCMSComponentModel>)slots.stream().flatMap(slot -> CollectionUtils.emptyIfNull(slot.getCmsComponents()).stream().filter(Objects::nonNull)).collect(Collectors.toSet());
        relatedItems.addAll(cmsComponentModels);
        Objects.requireNonNull(relatedItems);
        getCmsRestrictionService().getOwnRestrictionsForPage(pageModel, pageModel.getCatalogVersion()).stream().filter(Objects::nonNull).forEachOrdered(relatedItems::add);
        relatedItems.addAll(getChildComponentsAndRelatedRestrictions(cmsComponentModels, pageModel.getCatalogVersion()));
        return relatedItems;
    }


    public Set<CMSItemModel> getChildComponentsAndRelatedRestrictions(Set<AbstractCMSComponentModel> cmsComponentModels, CatalogVersionModel catalogVersionModel)
    {
        Set<CMSItemModel> relatedItems = new HashSet<>();
        if(!cmsComponentModels.isEmpty())
        {
            Set<AbstractCMSComponentModel> childrenComponents = (Set<AbstractCMSComponentModel>)cmsComponentModels.stream().flatMap(component -> getCmsComponentService().getAllChildren(component).stream().filter(Objects::nonNull)).collect(Collectors.toSet());
            relatedItems.addAll(childrenComponents);
            Objects.requireNonNull(relatedItems);
            getCmsRestrictionService().getOwnRestrictionsForComponents(cmsComponentModels, catalogVersionModel).stream().forEachOrdered(relatedItems::add);
            if(!childrenComponents.isEmpty())
            {
                Objects.requireNonNull(relatedItems);
                getCmsRestrictionService().getOwnRestrictionsForComponents(childrenComponents, catalogVersionModel).stream().forEachOrdered(relatedItems::add);
            }
        }
        return relatedItems;
    }


    protected CMSPageService getCmsPageService()
    {
        return this.cmsPageService;
    }


    protected CMSRestrictionService getCmsRestrictionService()
    {
        return this.cmsRestrictionService;
    }


    protected CMSComponentService getCmsComponentService()
    {
        return this.cmsComponentService;
    }
}
