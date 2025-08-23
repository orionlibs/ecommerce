package de.hybris.platform.core;

import de.hybris.platform.core.internal.BaseLazyLoadItemList;
import de.hybris.platform.jalo.Item;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public class LazyLoadItemList<E> extends BaseLazyLoadItemList<E>
{
    private static final Logger LOG = Logger.getLogger(LazyLoadItemList.class.getName());
    public static final String PREFETCH_SIZE_PROPERTY = "lazy.pkcollection.prefetchsize";
    public static final int DEFAULT_PREFETCH_SIZE = 100;
    public static final LazyLoadItemList EMPTY_LIST = new LazyLoadItemList(null, Collections.EMPTY_LIST, 100);
    @Deprecated(since = "ages", forRemoval = true)
    public static final int DEFUALT_PREFETCH_SIZE = 100;
    private volatile transient BufferedPage<E> currentBufferedPage;


    public LazyLoadItemList()
    {
        this(null, Collections.EMPTY_LIST, 100);
    }


    public LazyLoadItemList(Set<PK> prefetchLanguages, List<PK> pks, int prefetchSize)
    {
        this(prefetchLanguages, pks, prefetchSize, true);
    }


    protected LazyLoadItemList(Set<PK> prefetchLanguages, List<PK> pks, int prefetchSize, boolean modifable)
    {
        super(prefetchSize, prefetchLanguages, pks, modifable);
    }


    public E get(int index) throws IndexOutOfBoundsException
    {
        assureIndex(index);
        return getBuffered(index);
    }


    protected E getBuffered(int listPos)
    {
        BufferedPage<E> page = getOrSwitchBufferedPage(listPos);
        E ret = (E)page.get(listPos);
        if(ret instanceof Item)
        {
            Item cacheBoundItem = ((Item)ret).getCacheBoundItem();
            if(ret != cacheBoundItem)
            {
                synchronized(this)
                {
                    page.set(listPos, cacheBoundItem);
                }
                return (E)cacheBoundItem;
            }
        }
        return ret;
    }


    protected void invalidateBuffer()
    {
        this.currentBufferedPage = null;
    }


    protected BufferedPage<E> switchPage(int listPos)
    {
        int start = listPos / getPreFetchSize() * getPreFetchSize();
        int end = Math.min(start + getPreFetchSize(), size());
        List<E> loadedItems = new ArrayList<>(loadPage(getPKListInternal().subList(start, end)));
        return new BufferedPage(loadedItems, start);
    }


    protected List<E> loadPage(List<PK> pks)
    {
        return (List<E>)WrapperFactory.getCachedItems(
                        Registry.getCurrentTenantNoFallback().getCache(), pks,
                        getPrefetchLanguages(), true, true);
    }


    public E set(int index, Object element)
    {
        if(!isModifiable())
        {
            throw new UnsupportedOperationException("LazyLoadItemList " + this + " is not modifiable");
        }
        assureIndex(index);
        if(element == null)
        {
            getPKListInternal().set(index, null);
            BufferedPage<E> page = getBufferedPageIfLoaded(index);
            if(page != null)
            {
                page.set(index, null);
            }
        }
        else if(element instanceof Item)
        {
            getPKListInternal().set(index, ((Item)element).getPK());
            BufferedPage<E> page = getBufferedPageIfLoaded(index);
            if(page != null)
            {
                page.set(index, element);
            }
        }
        else if(element instanceof PK)
        {
            getPKListInternal().set(index, (PK)element);
            BufferedPage<E> page = getBufferedPageIfLoaded(index);
            if(page != null)
            {
                invalidateBuffer();
            }
        }
        else
        {
            throw new IllegalArgumentException("illegal element type for LazyLoadItemList - must be Item or PK");
        }
        return null;
    }


    public void add(int index, Object element)
    {
        if(!isModifiable())
        {
            throw new UnsupportedOperationException("LazyLoadItemList " + this + " is not modifiable");
        }
        if(element == null)
        {
            getPKListInternal().add(index, null);
            BufferedPage<E> page = getBufferedPageIfLoaded(index);
            if(page != null)
            {
                page.add(index, null);
            }
        }
        else if(element instanceof Item)
        {
            getPKListInternal().add(index, ((Item)element).getPK());
            BufferedPage<E> page = getBufferedPageIfLoaded(index);
            if(page != null)
            {
                page.add(index, element);
            }
        }
        else if(element instanceof PK)
        {
            getPKListInternal().add(index, (PK)element);
            BufferedPage<E> page = getBufferedPageIfLoaded(index);
            if(page != null)
            {
                invalidateBuffer();
            }
        }
        else
        {
            throw new IllegalArgumentException("illegal element type for LazyLoadItemList - must be Item or PK");
        }
    }


    public E remove(int index)
    {
        if(!isModifiable())
        {
            throw new UnsupportedOperationException("LazyLoadItemList " + this + " is not modifiable");
        }
        getPKListInternal().remove(index);
        BufferedPage<E> holder = getBufferedPageIfLoaded(index);
        if(holder != null)
        {
            holder.remove(index);
        }
        return null;
    }


    public void clear()
    {
        if(!isModifiable())
        {
            throw new UnsupportedOperationException("LazyLoadItemList " + this + " is not modifiable");
        }
        getPKListInternal().clear();
        invalidateBuffer();
    }


    @Deprecated(since = "4.8", forRemoval = true)
    public void setPreFetchSize(int preFetchSize)
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "4.8", forRemoval = true)
    public boolean isIgnoreMissing()
    {
        return true;
    }


    @Deprecated(since = "4.8", forRemoval = true)
    public void setIgnoreMissing(boolean ignoreMissing)
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "4.8", forRemoval = true)
    public void setModifiable(boolean modifiable)
    {
        throw new UnsupportedOperationException();
    }


    protected BufferedPage<E> getCurrentBufferedPage()
    {
        return this.currentBufferedPage;
    }


    protected BufferedPage<E> getOrSwitchBufferedPage(int totalIndex)
    {
        return getOrSwitchBufferedPage(totalIndex, true);
    }


    protected BufferedPage<E> getBufferedPageIfLoaded(int totalIndex)
    {
        return getOrSwitchBufferedPage(totalIndex, false);
    }


    private BufferedPage<E> getOrSwitchBufferedPage(int totalIndex, boolean createIfNotExisting)
    {
        BufferedPage<E> page = this.currentBufferedPage;
        if(page == null || !page.isBufferValid(totalIndex))
        {
            page = null;
            if(createIfNotExisting)
            {
                page = switchBufferedPage(totalIndex);
            }
        }
        return page;
    }


    protected BufferedPage<E> switchBufferedPage(int totalIndex)
    {
        return switchBufferedPageSynchronized(totalIndex);
    }


    protected final synchronized BufferedPage<E> switchBufferedPageSynchronized(int totalIndex)
    {
        return switchBufferedPageNoLock(totalIndex);
    }


    protected final BufferedPage<E> switchBufferedPageNoLock(int totalIndex)
    {
        BufferedPage<E> page = this.currentBufferedPage;
        if(page == null || !page.isBufferValid(totalIndex))
        {
            page = switchPage(totalIndex);
            this.currentBufferedPage = page;
        }
        return page;
    }
}
