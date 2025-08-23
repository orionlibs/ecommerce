package de.hybris.platform.core.internal;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.util.config.ConfigIntf;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public abstract class BaseLazyLoadItemList<E> extends AbstractList<E> implements Serializable
{
    private static final Logger LOG = Logger.getLogger(BaseLazyLoadItemList.class.getName());
    protected static final int DEFAULT_CONFIG_PREFETCH_SIZE = 100;
    protected static final String PREFETCH_SIZE_PROPERTY = "lazy.pkcollection.prefetchsize";
    public static final int DEFAULT_PREFETCH_SIZE = -1;
    private final boolean modifiable;
    private final int preFetchSize;
    private final List<PK> pkList;
    private final transient Set<PK> prefetchLanguages;


    protected BaseLazyLoadItemList(int preFetchSize, Set<PK> prefetchLanguages, List<PK> pkList, boolean modifiable)
    {
        this.preFetchSize = calculatePreFetchSize(preFetchSize);
        this.prefetchLanguages = (prefetchLanguages == null) ? null : new HashSet<>(prefetchLanguages);
        this.pkList = new ArrayList<>(pkList);
        this.modifiable = modifiable;
    }


    public String toString()
    {
        return "LazyList[" + this.pkList + "]";
    }


    public PK getPK(int index) throws IndexOutOfBoundsException
    {
        return this.pkList.get(index);
    }


    public List<PK> getPKList()
    {
        return Collections.unmodifiableList(this.pkList);
    }


    protected List<PK> getPKListInternal()
    {
        return this.pkList;
    }


    public E set(int index, Object element)
    {
        throw new UnsupportedOperationException();
    }


    public E remove(int index)
    {
        throw new UnsupportedOperationException();
    }


    public void clear()
    {
        throw new UnsupportedOperationException();
    }


    protected void assureIndex(int listPos) throws IndexOutOfBoundsException
    {
        if(listPos < 0)
        {
            throw new IndexOutOfBoundsException("0 > " + listPos);
        }
        if(listPos >= size())
        {
            throw new IndexOutOfBoundsException("" + listPos + " >= " + listPos);
        }
    }


    public int size()
    {
        return this.pkList.size();
    }


    public boolean contains(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        if(obj instanceof Item)
        {
            PK pk = ((Item)obj).getPK();
            return this.pkList.contains(pk);
        }
        if(obj instanceof PK)
        {
            return this.pkList.contains(obj);
        }
        throw new IllegalArgumentException("illegal element type for LazyLoadItemList - must be Item or PK");
    }


    public Iterator<E> iterator()
    {
        if(isModifiable())
        {
            return super.iterator();
        }
        return (Iterator<E>)new Object(this);
    }


    public final int hashCode()
    {
        return this.pkList.hashCode();
    }


    public final boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        if(obj instanceof BaseLazyLoadItemList)
        {
            return this.pkList.equals(((BaseLazyLoadItemList)obj).getPKListInternal());
        }
        return super.equals(obj);
    }


    public int getPreFetchSize()
    {
        return this.preFetchSize;
    }


    private int calculatePreFetchSize(int preFetchSize)
    {
        int tempprefetchsize;
        if(preFetchSize > -1)
        {
            tempprefetchsize = preFetchSize;
        }
        else
        {
            tempprefetchsize = getConfigBasedPrefetchSize();
        }
        return (tempprefetchsize < 1) ? 100 : tempprefetchsize;
    }


    private int getConfigBasedPrefetchSize()
    {
        int tempprefetchsize = getConfig().getInt("lazy.pkcollection.prefetchsize", 100);
        if(tempprefetchsize < 1)
        {
            LOG.warn("Wrong parameter lazy.pkcollection.prefetchsize=" + tempprefetchsize + ". Must be greater than 0.");
        }
        return tempprefetchsize;
    }


    public Set<PK> getPrefetchLanguages()
    {
        return this.prefetchLanguages;
    }


    public boolean isModifiable()
    {
        return this.modifiable;
    }


    protected ConfigIntf getConfig()
    {
        return Registry.getCurrentTenant().getConfig();
    }


    public abstract E get(int paramInt);
}
