package de.hybris.platform.regioncache;

import de.hybris.platform.cache.CacheStatisticsEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheStatistics implements CacheStatisticsEntry, Comparable<Object>
{
    private final StatisticsHolder general = new StatisticsHolder();
    private final ConcurrentMap<Object, StatisticsHolder> typeStats = new ConcurrentHashMap<>();


    private StatisticsHolder getStatisticsForType(Object typeCode)
    {
        String typeStr = String.valueOf(typeCode);
        StatisticsHolder holder = this.typeStats.get(typeStr);
        if(holder == null)
        {
            holder = new StatisticsHolder();
            StatisticsHolder tmp = this.typeStats.putIfAbsent(typeStr, holder);
            if(tmp != null)
            {
                holder = tmp;
            }
        }
        return holder;
    }


    public void hit(Object typeCode)
    {
        this.general.hits.incrementAndGet();
        (getStatisticsForType(typeCode)).hits.incrementAndGet();
    }


    public void fetched(Object typeCode)
    {
        this.general.fetches.incrementAndGet();
        (getStatisticsForType(typeCode)).fetches.incrementAndGet();
    }


    public void missed(Object typeCode)
    {
        this.general.misses.incrementAndGet();
        (getStatisticsForType(typeCode)).misses.incrementAndGet();
    }


    public void evicted(Object typeCode)
    {
        this.general.evictions.incrementAndGet();
        (getStatisticsForType(typeCode)).evictions.incrementAndGet();
    }


    public void invalidated(Object typeCode)
    {
        this.general.invalidations.incrementAndGet();
        (getStatisticsForType(typeCode)).invalidations.incrementAndGet();
    }


    public long getHits()
    {
        return this.general.hits.longValue();
    }


    public long getHits(Object typeCode)
    {
        return (getStatisticsForType(typeCode)).hits.longValue();
    }


    public long getFetches()
    {
        return this.general.fetches.longValue();
    }


    public long getFetches(Object typeCode)
    {
        return (getStatisticsForType(typeCode)).fetches.longValue();
    }


    public long getMisses()
    {
        return this.general.misses.longValue();
    }


    public long getMisses(Object typeCode)
    {
        return (getStatisticsForType(typeCode)).misses.longValue();
    }


    public long getEvictions()
    {
        return this.general.evictions.longValue();
    }


    public long getEvictions(Object typeCode)
    {
        return (getStatisticsForType(typeCode)).evictions.longValue();
    }


    public long getInvalidations()
    {
        return this.general.invalidations.longValue();
    }


    public long getInvalidations(Object typeCode)
    {
        return (getStatisticsForType(typeCode)).invalidations.longValue();
    }


    public long getInstanceCount()
    {
        return getMisses() - getInvalidations() - getEvictions();
    }


    public long getInstanceCount(Object typeCode)
    {
        return getMisses(typeCode) - getInvalidations(typeCode) - getEvictions(typeCode);
    }


    public Collection<Object> getTypes()
    {
        return this.typeStats.keySet();
    }


    public void clear()
    {
        this.typeStats.clear();
        this.general.hits.set(0L);
        this.general.fetches.set(0L);
        this.general.misses.set(0L);
        this.general.evictions.set(0L);
        this.general.invalidations.set(0L);
    }


    public String getKeyString()
    {
        return Arrays.deepToString(getTypes().toArray());
    }


    public long getHitCount()
    {
        return getHits();
    }


    public long getMissCount()
    {
        return getMisses();
    }


    public int getFactor()
    {
        long hitCount = getHitCount();
        if(hitCount == 0L)
        {
            return 0;
        }
        return (int)(100L * getHitCount() / (getHitCount() + getMissCount()));
    }


    public int getFactor(Object typeCode)
    {
        long hitCount = getHits(typeCode);
        if(hitCount == 0L)
        {
            return 0;
        }
        return (int)(100L * getHits(typeCode) / (getHits(typeCode) + getMisses(typeCode)));
    }


    public int compareTo(Object object)
    {
        if(!(object instanceof CacheStatisticsEntry))
        {
            return -1;
        }
        CacheStatisticsEntry tmp = (CacheStatisticsEntry)object;
        return (int)(tmp.getHitCount() - getHitCount());
    }
}
