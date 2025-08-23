package de.hybris.platform.servicelayer.search.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LazyLoadModelList<V> extends LazyLoadItemList<V>
{
    private final List<Class> expectedClassList;
    private final ItemObjectResolver itemResolver;
    private static final ItemObjectResolver EMPTY_ITEM_RESOLVER = (ItemObjectResolver)new NullItemObjectResolver();
    public static final LazyLoadModelList EMPTY_MODEL_LIST = new LazyLoadModelList(LazyLoadItemList.EMPTY_LIST, 100, Collections.EMPTY_LIST, EMPTY_ITEM_RESOLVER);


    public LazyLoadModelList(LazyLoadItemList llItemList, int prefetchSize)
    {
        this(llItemList, prefetchSize, null, null);
    }


    public LazyLoadModelList(LazyLoadItemList llItemList, int prefetchSize, List<Class<?>> expectedClassList, ItemObjectResolver itemResolver)
    {
        super(llItemList.getPrefetchLanguages(), llItemList.getPKList(), prefetchSize, false);
        Preconditions.checkNotNull(expectedClassList, "expected class list is not set");
        Preconditions.checkNotNull(itemResolver, "item resolver is not set");
        this.expectedClassList = expectedClassList;
        this.itemResolver = itemResolver;
    }


    protected List<V> loadPage(List pks)
    {
        ArrayList<V> modelsList = new ArrayList<>(getPreFetchSize());
        ItemObjectResolver<V> itemResolver = getItemResolver();
        if(itemResolver.preloadItems(pks))
        {
            for(PK pk : pks)
            {
                if(pk == null)
                {
                    modelsList.add(null);
                    continue;
                }
                try
                {
                    modelsList.add((V)itemResolver.resolve(pk, getExpectedClassList()));
                }
                catch(Exception e)
                {
                    modelsList.add(null);
                }
            }
        }
        else
        {
            for(Item sourceItem : super.loadPage(pks))
            {
                if(sourceItem == null)
                {
                    modelsList.add(null);
                    continue;
                }
                modelsList.add((V)itemResolver.resolve(sourceItem, getExpectedClassList()));
            }
        }
        return modelsList;
    }


    public boolean isModifiable()
    {
        return false;
    }


    public boolean contains(Object obj)
    {
        if(obj instanceof ItemModel)
        {
            return super.contains(((ItemModel)obj).getPk());
        }
        return super.contains(obj);
    }


    protected ItemObjectResolver<V> getItemResolver()
    {
        return this.itemResolver;
    }


    protected List<Class> getExpectedClassList()
    {
        return this.expectedClassList;
    }


    @Deprecated(since = "4.8", forRemoval = true)
    public void setExpectedClassList(List<Class<?>> expectedClassList)
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "4.8", forRemoval = true)
    public void setItemResolver(ItemObjectResolver itemResolver)
    {
        throw new UnsupportedOperationException();
    }
}
