package de.hybris.platform.cache.relation;

import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class RelationCacheRegion implements CacheRegion
{
    private static final String NAME = "relationCacheRegion";
    protected final CacheStatistics stats = new CacheStatistics();
    private final RelationsCacheConfiguration configuration = new RelationsCacheConfiguration();
    private final RelationsCache cache = new RelationsCache(this.configuration, createEvictionListener());
    private volatile boolean statsEnabled;
    private String[] handledTypes;


    public RelationCacheRegion(boolean statsEnabled)
    {
        this.statsEnabled = statsEnabled;
    }


    public Object getWithLoader(CacheKey key, CacheValueLoader loader)
    {
        if(!(key instanceof RelationCacheKey))
        {
            return null;
        }
        RelationCacheKey relationKey = (RelationCacheKey)key;
        Object value = this.cache.get(relationKey);
        if(value == null)
        {
            Object loaded = loader.load(key);
            this.cache.put(relationKey, loaded);
            if(this.statsEnabled)
            {
                this.stats.fetched(relationKey.getFullyQualifiedRelationSideId());
                this.stats.missed(relationKey.getFullyQualifiedRelationSideId());
            }
            return loaded;
        }
        if(this.statsEnabled)
        {
            this.stats.hit(relationKey.getFullyQualifiedRelationSideId());
        }
        return value;
    }


    public Object get(CacheKey key)
    {
        if(!(key instanceof RelationCacheKey))
        {
            return null;
        }
        RelationCacheKey relationKey = (RelationCacheKey)key;
        Object value = this.cache.get(relationKey);
        if(this.statsEnabled)
        {
            if(value != null)
            {
                this.stats.hit(relationKey.getFullyQualifiedRelationSideId());
            }
            else
            {
                this.stats.missed(relationKey.getFullyQualifiedRelationSideId());
            }
        }
        return value;
    }


    public Object invalidate(CacheKey key, boolean fireEventsEvenIfNotRemoved)
    {
        return remove(key, fireEventsEvenIfNotRemoved);
    }


    public String getName()
    {
        return "relationCacheRegion";
    }


    public String[] getHandledTypes()
    {
        return (String[])this.handledTypes.clone();
    }


    public CacheStatistics getCacheRegionStatistics()
    {
        return this.stats;
    }


    public long getCacheMaxEntries()
    {
        return this.cache.getThreeLayersCache()
                        .entrySet()
                        .stream()
                        .flatMap(tenant -> ((Map)tenant.getValue()).entrySet().stream())
                        .mapToLong(relationId -> this.configuration.getCapacityForRelation((String)relationId.getKey()) * ((Map)relationId.getValue()).size())
                        .sum();
    }


    public void clearCache()
    {
        this.cache.clear();
        this.stats.clear();
    }


    public void registerLifecycleCallback(CacheLifecycleCallback callback)
    {
    }


    public long getMaxReachedSize()
    {
        return getAllKeys().size();
    }


    public Collection<CacheKey> getAllKeys()
    {
        return this.cache.getAllKeys();
    }


    public Object remove(CacheKey key, boolean fireEvents)
    {
        if(!(key instanceof RelationCacheKey))
        {
            return null;
        }
        RelationCacheKey relationKey = (RelationCacheKey)key;
        if(relationKey.isForRelation())
        {
            this.cache.removeRelation(relationKey);
            this.stats.invalidated(relationKey.getInvalidationIdentifier());
            return null;
        }
        Object element = this.cache.remove(relationKey);
        if(element != null)
        {
            this.stats.invalidated(relationKey.getFullyQualifiedRelationSideId());
        }
        return element;
    }


    public boolean containsKey(CacheKey key)
    {
        if(!(key instanceof RelationCacheKey))
        {
            return false;
        }
        return this.cache.containsKey((RelationCacheKey)key);
    }


    public void setStatsEnabled(boolean enabled)
    {
        this.statsEnabled = enabled;
    }


    public boolean isStatsEnabled()
    {
        return this.statsEnabled;
    }


    public String toString()
    {
        return "Name: relationCacheRegion, size: " + this.cache.getAllKeys().size() + ", types: " + Arrays.deepToString((Object[])
                        getHandledTypes()) + ", statistics: " + this.statsEnabled + ", class: " +
                        getClass();
    }


    public void setHandledTypes(String[] handledTypes)
    {
        this.handledTypes = (String[])handledTypes.clone();
    }


    private EvictionListener<RelationCacheKey, Object> createEvictionListener()
    {
        return (relationKey, value) -> {
            if(this.statsEnabled)
            {
                this.stats.evicted(relationKey.getFullyQualifiedRelationSideId());
            }
        };
    }
}
