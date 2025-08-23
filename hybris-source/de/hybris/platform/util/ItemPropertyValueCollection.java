package de.hybris.platform.util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class ItemPropertyValueCollection extends ArrayList<ItemPropertyValue>
{
    static final long serialVersionUID = -6844582406938289545L;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_LIST = 1;
    public static final int TYPE_SET = 2;
    public static final int TYPE_SORTEDSET = 3;
    private int wrapedCollectionType = 0;


    public static int getWrapedTypeOfCollection(Collection<?> coll)
    {
        if(coll != null)
        {
            if(coll instanceof ItemPropertyValueCollection)
            {
                return ((ItemPropertyValueCollection)coll).getWrapedCollectionType();
            }
            if(coll instanceof java.util.SortedSet)
            {
                return 3;
            }
            if(coll instanceof java.util.Set)
            {
                return 2;
            }
            if(coll instanceof java.util.List)
            {
                return 1;
            }
        }
        return 0;
    }


    public Collection createNewWrappedCollection()
    {
        switch(getWrapedCollectionType())
        {
            case 1:
                return new ArrayList(size());
            case 2:
                return new LinkedHashSet(size());
            case 3:
                return new TreeSet();
        }
        return new ArrayList(size());
    }


    public ItemPropertyValueCollection(Collection<? extends ItemPropertyValue> coll)
    {
        super(coll);
        checkCollection(coll);
        setWrapedCollectionType(getWrapedTypeOfCollection(coll));
    }


    private ItemPropertyValueCollection(ItemPropertyValueCollection coll)
    {
        super(coll);
        this.wrapedCollectionType = coll.wrapedCollectionType;
    }


    private ItemPropertyValueCollection(ItemPropertyValueCollection coll, BitSet excludeIndexes)
    {
        super(coll.size() - excludeIndexes.cardinality());
        int i = 0;
        for(ItemPropertyValue pv : coll)
        {
            if(!excludeIndexes.get(i++))
            {
                add(pv);
            }
        }
        this.wrapedCollectionType = coll.wrapedCollectionType;
    }


    public Object clone()
    {
        return new ItemPropertyValueCollection(this);
    }


    public ItemPropertyValueCollection cloneWithoutInvalid(BitSet invalidIndexes)
    {
        return new ItemPropertyValueCollection(this, invalidIndexes);
    }


    private static final void checkElement(Object element) throws IllegalArgumentException
    {
        if(element != null && !(element instanceof ItemPropertyValue))
        {
            throw new IllegalArgumentException("ItemPropertyValueCollection can only hold ItemPropertyValue elements (tried to insert " + element
                            .getClass() + ")");
        }
    }


    private static final void checkCollection(Collection coll) throws IllegalArgumentException
    {
        for(Object o : coll)
        {
            checkElement(o);
        }
    }


    public void add(int index, ItemPropertyValue element)
    {
        super.add(index, element);
    }


    public boolean add(ItemPropertyValue o)
    {
        return super.add(o);
    }


    public boolean addAll(Collection<? extends ItemPropertyValue> c)
    {
        checkCollection(c);
        return super.addAll(c);
    }


    public boolean addAll(int index, Collection<? extends ItemPropertyValue> c)
    {
        checkCollection(c);
        return super.addAll(index, c);
    }


    public ItemPropertyValue set(int index, ItemPropertyValue element)
    {
        return super.set(index, element);
    }


    public int getWrapedCollectionType()
    {
        return this.wrapedCollectionType;
    }


    public void setWrapedCollectionType(int wrapedCollectionType)
    {
        this.wrapedCollectionType = wrapedCollectionType;
    }


    public ItemPropertyValueCollection()
    {
    }
}
