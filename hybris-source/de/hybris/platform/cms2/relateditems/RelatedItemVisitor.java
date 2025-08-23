package de.hybris.platform.cms2.relateditems;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.List;
import java.util.function.Predicate;

public interface RelatedItemVisitor
{
    List<CMSItemModel> getRelatedItems(ItemModel paramItemModel);


    List<CMSItemModel> getRelatedItems(ItemModel paramItemModel, InterceptorContext paramInterceptorContext);


    Predicate<ItemModel> getConstrainedBy();
}
