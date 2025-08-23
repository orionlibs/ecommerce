package de.hybris.platform.servicelayer.search.impl;

import de.hybris.platform.core.AbstractLazyLoadMultiColumnList;
import de.hybris.platform.core.LazyLoadMultiColumnList;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;
import java.util.List;

public class LazyLoadMultiColumnModelList extends AbstractLazyLoadMultiColumnList<LazyLoadModelList>
{
    LazyLoadMultiColumnModelList(LazyLoadMultiColumnList original, int itemPrefetchSize, List<Class<?>> signature, ItemObjectResolver modelDataResolver)
    {
        super((AbstractLazyLoadMultiColumnList)original, (AbstractLazyLoadMultiColumnList.CalculateLazyLoadListBody)new Object(original, itemPrefetchSize, signature, modelDataResolver));
    }


    protected LazyLoadModelList createEmptyItemList()
    {
        return LazyLoadModelList.EMPTY_MODEL_LIST;
    }


    protected Object fetchSource(int marker, int position)
    {
        LazyLoadModelList llModelList = (LazyLoadModelList)this.wrappedItemList;
        return llModelList.getItemResolver().resolve(position, llModelList.get(marker - 1), llModelList.getExpectedClassList());
    }


    protected Object wrapObject(Object original)
    {
        return ((LazyLoadModelList)this.wrappedItemList).getItemResolver().unresolve(super.wrapObject(original));
    }
}
