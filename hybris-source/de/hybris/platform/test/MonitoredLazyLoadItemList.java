package de.hybris.platform.test;

import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import java.util.List;
import java.util.Set;

public class MonitoredLazyLoadItemList<E> extends LazyLoadItemList<E>
{
    private transient boolean notemptyPageBuffer = false;


    public MonitoredLazyLoadItemList(Set<PK> prefetchLanguages, List<PK> pks, int prefetchSize)
    {
        super(prefetchLanguages, pks, prefetchSize);
    }


    protected E getBuffered(int listPos)
    {
        E result = (E)super.getBuffered(listPos);
        LazyLoadItemList.BufferedPage<E> page = getBufferedPageIfLoaded(listPos);
        this.notemptyPageBuffer = (page != null && !page.isEmpty());
        return result;
    }


    public boolean isNotEmptyPageBuffer()
    {
        return this.notemptyPageBuffer;
    }
}
