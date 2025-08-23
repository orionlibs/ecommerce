package de.hybris.platform.util.collections.fast;

import de.hybris.platform.util.collections.fast.procedures.YLongAndObjectProcedure;
import de.hybris.platform.util.collections.fast.procedures.YLongProcedure;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public class YLongToObjectMap<V>
{
    private final Long2ObjectOpenHashMap<V> target;


    public YLongToObjectMap()
    {
        this.target = new Long2ObjectOpenHashMap();
    }


    public YLongToObjectMap(int initialCapacity)
    {
        this.target = new Long2ObjectOpenHashMap(initialCapacity);
    }


    public V put(long key, V value)
    {
        return (V)this.target.put(key, value);
    }


    public V get(long key)
    {
        return (V)this.target.get(key);
    }


    public boolean containsKey(long key)
    {
        return this.target.containsKey(key);
    }


    public V remove(long key)
    {
        return (V)this.target.remove(key);
    }


    public void clear()
    {
        this.target.clear();
    }


    public int size()
    {
        return this.target.size();
    }


    public boolean isEmpty()
    {
        return this.target.isEmpty();
    }


    public void forEachEntry(YLongAndObjectProcedure<V> procedure)
    {
        if(procedure == null)
        {
            throw new IllegalArgumentException("procedure can't be null");
        }
        ObjectIterator<Long2ObjectMap.Entry<V>> iterator = this.target.long2ObjectEntrySet().fastIterator();
        Long2ObjectMap.Entry<V> entry = null;
        while(iterator.hasNext())
        {
            entry = (Long2ObjectMap.Entry<V>)iterator.next();
            procedure.execute(entry.getLongKey(), entry.getValue());
        }
    }


    public void forEachKey(YLongProcedure procedure)
    {
        if(procedure == null)
        {
            throw new IllegalArgumentException("procedure can't be null");
        }
        LongIterator iterator = this.target.keySet().iterator();
        while(iterator.hasNext())
        {
            procedure.execute(iterator.nextLong());
        }
    }
}
