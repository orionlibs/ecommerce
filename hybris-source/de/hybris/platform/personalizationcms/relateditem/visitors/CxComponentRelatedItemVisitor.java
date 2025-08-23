package de.hybris.platform.personalizationcms.relateditem.visitors;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.RelatedItemVisitor;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationcms.container.dao.CxContainerDao;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class CxComponentRelatedItemVisitor implements RelatedItemVisitor
{
    private Predicate<ItemModel> visitorPredicate;
    private CxContainerDao cxContainerDao;
    private CMSPageService cmsPageService;
    private CMSComponentService cmsComponentService;


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel)
    {
        SimpleCMSComponentModel cmsComponentModel = (SimpleCMSComponentModel)itemModel;
        List<AbstractCMSComponentModel> relatedComponentModels = new ArrayList<>();
        relatedComponentModels.add(cmsComponentModel);
        relatedComponentModels.addAll(getCmsComponentService().getAllParents((AbstractCMSComponentModel)cmsComponentModel));
        List<CxCmsComponentContainerModel> containers = (List<CxCmsComponentContainerModel>)relatedComponentModels.stream().filter(abstractCMSComponentModel -> getConstrainedBy().test(abstractCMSComponentModel))
                        .flatMap(relatedComponentModel -> CollectionUtils.emptyIfNull(getCxContainerDao().getCxContainersByDefaultComponent((SimpleCMSComponentModel)relatedComponentModel)).stream()).collect(Collectors.toList());
        List<ContentSlotModel> slots = (List<ContentSlotModel>)containers.stream().flatMap(container -> CollectionUtils.emptyIfNull(container.getSlots()).stream()).filter(slot -> Objects.nonNull(slot.getPk())).collect(Collectors.toList());
        Collection<AbstractPageModel> pages = getCmsPageService().getPagesForContentSlots(slots);
        List<CMSItemModel> relatedItems = new ArrayList<>();
        relatedItems.addAll(relatedComponentModels);
        relatedItems.addAll(containers);
        relatedItems.addAll(slots);
        relatedItems.addAll(pages);
        return relatedItems;
    }


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel, InterceptorContext interceptorContext)
    {
        if(Objects.nonNull(interceptorContext) && interceptorContext.getDirtyAttributes(itemModel).size() == 1 && interceptorContext
                        .getDirtyAttributes(itemModel).containsKey("parents"))
        {
            return new ArrayList<>();
        }
        return getRelatedItems(itemModel);
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


    protected CxContainerDao getCxContainerDao()
    {
        return this.cxContainerDao;
    }


    @Required
    public void setCxContainerDao(CxContainerDao cxContainerDao)
    {
        this.cxContainerDao = cxContainerDao;
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
