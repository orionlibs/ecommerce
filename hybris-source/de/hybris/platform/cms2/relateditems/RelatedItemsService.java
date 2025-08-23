package de.hybris.platform.cms2.relateditems;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.List;

public interface RelatedItemsService
{
    List<CMSItemModel> getRelatedItems(ItemModel paramItemModel, InterceptorContext paramInterceptorContext);


    List<CMSItemModel> getRelatedItems(ItemModel paramItemModel);
}
