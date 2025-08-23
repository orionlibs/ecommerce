package de.hybris.platform.cms2.relateditems.visitors.page;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.relateditems.RelatedItemVisitor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class ContentSlotForPageRelatedItemVisitor implements RelatedItemVisitor
{
    private Predicate<ItemModel> visitorPredicate;


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel, InterceptorContext interceptorContext)
    {
        return getRelatedItems(itemModel);
    }


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel)
    {
        ContentSlotForPageModel contentSlotForPageModel = (ContentSlotForPageModel)itemModel;
        List<CMSItemModel> relatedItems = new ArrayList<>();
        relatedItems.addAll((Collection)Arrays.asList((Object[])new AbstractPageModel[] {contentSlotForPageModel.getPage()}));
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
}
