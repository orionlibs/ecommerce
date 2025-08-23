package de.hybris.platform.core;

import java.util.List;
import java.util.Set;

public class LazyLoadMultiColumnList extends AbstractLazyLoadMultiColumnList<LazyLoadItemList>
{
    public LazyLoadMultiColumnList(LazyLoadMultiColumnList original)
    {
        super(original, (AbstractLazyLoadMultiColumnList.CalculateLazyLoadListBody)new Object(original));
    }


    public LazyLoadMultiColumnList(List<List<Object>> originalRows, List<Class<?>> signature, Set<PK> prefetchLanguages, int prefetchSize, boolean mustWrapObjectsToo)
    {
        super(originalRows, signature, prefetchLanguages, prefetchSize, mustWrapObjectsToo);
    }


    protected List createEmptyItemList()
    {
        return (List)LazyLoadItemList.EMPTY_LIST;
    }


    protected List createItemList(Set<PK> prefetchLanguages, List<PK> itemPKs, int prefetchSize)
    {
        return (List)new LazyLoadItemList(prefetchLanguages, itemPKs, prefetchSize);
    }
}
