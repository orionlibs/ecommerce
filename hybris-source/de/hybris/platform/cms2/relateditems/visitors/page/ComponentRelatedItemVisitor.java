package de.hybris.platform.cms2.relateditems.visitors.page;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.RelatedItemVisitor;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class ComponentRelatedItemVisitor implements RelatedItemVisitor
{
    private Predicate<ItemModel> visitorPredicate;
    private CMSPageService cmsPageService;
    private CMSComponentService cmsComponentService;


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel, InterceptorContext interceptorContext)
    {
        if(Objects.nonNull(interceptorContext) && interceptorContext.getDirtyAttributes(itemModel).size() == 1 && (interceptorContext
                        .getDirtyAttributes(itemModel).containsKey("parents") || interceptorContext
                        .getDirtyAttributes(itemModel).containsKey("slots")))
        {
            return new ArrayList<>();
        }
        return getRelatedItems(itemModel);
    }


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel)
    {
        AbstractCMSComponentModel cmsComponentModel = (AbstractCMSComponentModel)itemModel;
        List<AbstractCMSComponentModel> relatedComponentModels = new ArrayList<>();
        relatedComponentModels.add(cmsComponentModel);
        relatedComponentModels.addAll(getCmsComponentService().getAllParents(cmsComponentModel));
        List<ContentSlotModel> contentSlotModels = (List<ContentSlotModel>)relatedComponentModels.stream().map(componentModel -> CollectionUtils.emptyIfNull(componentModel.getSlots())).flatMap(Collection::stream).filter(slot -> Objects.nonNull(slot.getPk())).collect(Collectors.toList());
        Collection<AbstractPageModel> pages = getCmsPageService().getPagesForContentSlots(contentSlotModels);
        List<CMSItemModel> relatedItems = new ArrayList<>();
        relatedItems.addAll(relatedComponentModels);
        relatedItems.addAll(contentSlotModels);
        relatedItems.addAll(pages);
        return relatedItems;
    }


    public Predicate<ItemModel> getConstrainedBy()
    {
        return this.visitorPredicate;
    }


    @Required
    public void setVisitorPredicate(Predicate<ItemModel> visitorPredicate)
    {
        this.visitorPredicate = visitorPredicate;
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


    public CMSComponentService getCmsComponentService()
    {
        return this.cmsComponentService;
    }


    public void setCmsComponentService(CMSComponentService cmsComponentService)
    {
        this.cmsComponentService = cmsComponentService;
    }
}
