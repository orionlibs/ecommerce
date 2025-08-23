package de.hybris.platform.core;

import de.hybris.platform.core.internal.BaseLazyLoadItemList;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;
import de.hybris.platform.util.collections.YFastFIFOMap;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public abstract class AbstractLazyLoadMultiColumnList<T extends LazyLoadItemList> extends AbstractList<List<Object>> implements Serializable
{
    protected static final Object NULL_MARKER = "null".intern();
    protected final BaseLazyLoadItemList wrappedItemList;
    private volatile Map<Integer, Object> wrappedObjects;
    private final List<LazyLoadMultiColumnListRow> rows;


    protected List createItemList(Set<PK> prefetchLanguages, List<PK> itemPKs, int prefetchSize)
    {
        throw new UnsupportedOperationException();
    }


    protected AbstractLazyLoadMultiColumnList(AbstractLazyLoadMultiColumnList original, CalculateLazyLoadListBody itemListProvider)
    {
        if(original != null)
        {
            List<LazyLoadMultiColumnListRow> rows = original.getRows();
            if(CollectionUtils.isNotEmpty(rows))
            {
                this.rows = copyRows(rows);
            }
            else
            {
                this.rows = Collections.EMPTY_LIST;
            }
            this.wrappedItemList = (BaseLazyLoadItemList)itemListProvider.calculate(original.getWrappedItemListPks());
        }
        else
        {
            this.rows = Collections.EMPTY_LIST;
            this.wrappedItemList = (BaseLazyLoadItemList)LazyLoadItemList.EMPTY_LIST;
        }
    }


    protected AbstractLazyLoadMultiColumnList(List<List<Object>> originalRows, List<Class<?>> signature, Set<PK> prefetchLanguages, int prefetchSize, boolean mustWrapObjectsToo)
    {
        Object[] result = calculateRowsAndItemList(originalRows, signature, prefetchLanguages, prefetchSize, mustWrapObjectsToo);
        this.rows = (List<LazyLoadMultiColumnListRow>)result[0];
        this.wrappedItemList = (BaseLazyLoadItemList)result[1];
    }


    final Object getFromRow(LazyLoadMultiColumnListRow row, int pos)
    {
        int marker = row.columnMarkers[pos];
        if(marker == 0)
        {
            return row.original.get(pos);
        }
        if(marker > 0)
        {
            return fetchSource(marker, pos);
        }
        return wrapObject(row, marker, pos);
    }


    protected Object fetchSource(int marker, int position)
    {
        return this.wrappedItemList.get(marker - 1);
    }


    private Object wrapObject(LazyLoadMultiColumnListRow row, int objectPosition, int columnPosition)
    {
        Integer key = Integer.valueOf(objectPosition);
        Map<Integer, Object> map = assureWrappedObjectMap();
        Object ret = map.get(key);
        if(ret == null)
        {
            ret = wrapObject(row.original.get(columnPosition));
            map.put(key, (ret != null) ? ret : NULL_MARKER);
        }
        return NULL_MARKER.equals(ret) ? null : ret;
    }


    private synchronized Map<Integer, Object> assureWrappedObjectMap()
    {
        YFastFIFOMap yFastFIFOMap;
        Map<Integer, Object> localMap = this.wrappedObjects;
        if(localMap == null)
        {
            yFastFIFOMap = new YFastFIFOMap(1000);
        }
        return this.wrappedObjects = (Map<Integer, Object>)yFastFIFOMap;
    }


    protected Object wrapObject(Object original)
    {
        return WrapperFactory.wrap(original);
    }


    public List<Object> get(int index)
    {
        return (List<Object>)this.rows.get(index);
    }


    public List<LazyLoadMultiColumnListRow> getRows()
    {
        return this.rows;
    }


    public boolean isWrappedItemListEmpty()
    {
        return this.wrappedItemList.isEmpty();
    }


    public int getWrappedPrefetchSize()
    {
        return this.wrappedItemList.getPreFetchSize();
    }


    public List<PK> getWrappedItemListPks()
    {
        return this.wrappedItemList.getPKList();
    }


    public Set<PK> getPrefetchedLanguages()
    {
        return this.wrappedItemList.getPrefetchLanguages();
    }


    public int size()
    {
        return this.rows.size();
    }


    private List<LazyLoadMultiColumnListRow> copyRows(List<LazyLoadMultiColumnListRow> oldRows)
    {
        List<LazyLoadMultiColumnListRow> newRows = null;
        for(LazyLoadMultiColumnListRow row : oldRows)
        {
            if(newRows == null)
            {
                newRows = new ArrayList<>(oldRows.size());
            }
            newRows.add(new LazyLoadMultiColumnListRow(this, row.getOriginal(), row.getColumnMarkers()));
        }
        return newRows;
    }


    private Object[] calculateRowsAndItemList(List<List<Object>> originalRows, List<Class<?>> signature, Set<PK> prefetchLanguages, int prefetchSize, boolean mustWrapObjectsToo)
    {
        BitSet[] _colSets = analzyeCols(signature, mustWrapObjectsToo);
        BitSet itemColumns = _colSets[0];
        BitSet objectColumns = _colSets[1];
        List<PK> itemPKs = new ArrayList<>(originalRows.size() * itemColumns.cardinality() + 1);
        List<LazyLoadMultiColumnListRow> rowsLocal = new ArrayList<>(originalRows.size());
        int objectCounter = -1;
        for(List<Object> row : originalRows)
        {
            int[] columnMarkers = new int[row.size()];
            int pos;
            for(pos = itemColumns.nextSetBit(0); pos > -1; pos = itemColumns.nextSetBit(pos + 1))
            {
                ItemPropertyValue ipv = (ItemPropertyValue)row.get(pos);
                if(ipv != null)
                {
                    columnMarkers[pos] = itemPKs.size() + 1;
                    itemPKs.add(ipv.getPK());
                }
            }
            for(pos = objectColumns.nextSetBit(0); pos > -1; pos = objectColumns.nextSetBit(pos + 1))
            {
                Object object = row.get(pos);
                if(object != null)
                {
                    if(object instanceof ItemPropertyValue)
                    {
                        columnMarkers[pos] = itemPKs.size() + 1;
                        itemPKs.add(((ItemPropertyValue)object).getPK());
                    }
                    else
                    {
                        columnMarkers[pos] = objectCounter;
                        objectCounter--;
                    }
                }
            }
            rowsLocal.add(new LazyLoadMultiColumnListRow(this, row, columnMarkers));
        }
        BaseLazyLoadItemList itemList = null;
        if(itemPKs.isEmpty())
        {
            itemList = (BaseLazyLoadItemList)createEmptyItemList();
        }
        else
        {
            itemList = (BaseLazyLoadItemList)createItemList(prefetchLanguages, itemPKs, prefetchSize);
        }
        return new Object[] {rowsLocal, itemList};
    }


    private BitSet[] analzyeCols(List<Class<?>> expectedColumnClassList, boolean mustWrapObjectsToo)
    {
        BitSet itemColumns = new BitSet(expectedColumnClassList.size());
        BitSet objectColumns = new BitSet(expectedColumnClassList.size());
        int index = 0;
        for(Class<?> cl : expectedColumnClassList)
        {
            if(ItemPropertyValue.class.isAssignableFrom(cl))
            {
                itemColumns.set(index);
            }
            else if(mustWrapObjectsToo && (Serializable.class.equals(cl) || ItemPropertyValueCollection.class.isAssignableFrom(cl)))
            {
                objectColumns.set(index);
            }
            index++;
        }
        return new BitSet[] {itemColumns, objectColumns};
    }


    protected abstract List createEmptyItemList();
}
