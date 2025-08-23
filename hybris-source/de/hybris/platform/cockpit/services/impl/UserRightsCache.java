package de.hybris.platform.cockpit.services.impl;

import de.hybris.platform.core.PK;
import java.util.Map;
import org.apache.commons.collections.map.LRUMap;

public class UserRightsCache
{
    private final Map<PK, CacheEntry> urMap;


    public UserRightsCache()
    {
        this(1000);
    }


    public UserRightsCache(int maxSize)
    {
        this.urMap = (Map<PK, CacheEntry>)new LRUMap(Math.max(1, maxSize));
    }


    public void addRight(PK relatedObject, String right, boolean value)
    {
        CacheEntry entry = this.urMap.get(relatedObject);
        if(entry == null)
        {
            entry = new CacheEntry(this);
            this.urMap.put(relatedObject, entry);
        }
        entry.addRight(right, value ? Boolean.TRUE : Boolean.FALSE);
    }


    public Boolean getRight(PK relatedObject, String right)
    {
        CacheEntry entry = this.urMap.get(relatedObject);
        if(entry != null)
        {
            return entry.getRight(right);
        }
        return null;
    }


    public void clear()
    {
        this.urMap.clear();
    }
}
