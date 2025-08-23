package de.hybris.platform.cms2.relateditems.visitors.page;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.relateditems.RelatedItemVisitor;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class SlotRelatedItemVisitor implements RelatedItemVisitor
{
    private Predicate<ItemModel> visitorPredicate;
    private CMSContentSlotDao cmsContentSlotDao;


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel, InterceptorContext interceptorContext)
    {
        return getRelatedItems(itemModel);
    }


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel)
    {
        if(Objects.isNull(itemModel.getPk()))
        {
            return Collections.emptyList();
        }
        ContentSlotModel contentSlotModel = (ContentSlotModel)itemModel;
        List<CMSItemModel> relatedItems = new ArrayList<>();
        relatedItems.add(contentSlotModel);
        relatedItems.addAll(getCmsContentSlotDao().findPagesByContentSlot(contentSlotModel));
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


    protected CMSContentSlotDao getCmsContentSlotDao()
    {
        return this.cmsContentSlotDao;
    }


    @Required
    public void setCmsContentSlotDao(CMSContentSlotDao cmsContentSlotDao)
    {
        this.cmsContentSlotDao = cmsContentSlotDao;
    }
}
