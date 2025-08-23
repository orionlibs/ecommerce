package de.hybris.platform.cache.impl;

import de.hybris.platform.cache.CacheStatisticsEntry;

public class DefaultCacheStatisticsEntry implements CacheStatisticsEntry, Comparable<Object>
{
    private final String keyString;
    private long hitCount;
    private long missCount;


    public DefaultCacheStatisticsEntry(Object[] key)
    {
        this.keyString = DefaultCache.keyToString(key);
    }


    public void update(boolean missed)
    {
        if(missed)
        {
            this.missCount++;
        }
        else
        {
            this.hitCount++;
        }
    }


    public int compareTo(Object object)
    {
        int diff = getFactor() - ((DefaultCacheStatisticsEntry)object).getFactor();
        if(diff != 0)
        {
            return diff;
        }
        long diff2 = getHitCount() + getMissCount() - ((DefaultCacheStatisticsEntry)object).getHitCount() + ((DefaultCacheStatisticsEntry)object).getMissCount();
        return (diff2 > 0L) ? -1 : 0;
    }


    public final boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        return ((DefaultCacheStatisticsEntry)obj).keyString.equals(this.keyString);
    }


    public final int hashCode()
    {
        return this.keyString.hashCode();
    }


    public String getKeyString()
    {
        return this.keyString;
    }


    public long getHitCount()
    {
        return this.hitCount;
    }


    public long getMissCount()
    {
        return this.missCount;
    }


    public int getFactor()
    {
        return (int)(100L * this.hitCount / (this.hitCount + this.missCount));
    }


    public String toString()
    {
        return this.keyString + ": " + this.keyString + "/" + this.hitCount;
    }
}
