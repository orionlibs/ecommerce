package de.hybris.platform.cache.relation;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import de.hybris.platform.regioncache.key.CacheKey;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RelationsCache
{
    private final Map<String, Map<String, Map<String, Map<RelationCacheKey, Object>>>> threeLayersCache = new ConcurrentHashMap<>();
    private final RelationsCacheConfiguration configuration;
    private final EvictionListener<RelationCacheKey, Object> evictionListener;


    public RelationsCache(RelationsCacheConfiguration configuration, EvictionListener<RelationCacheKey, Object> evictionListener)
    {
        this.configuration = configuration;
        this.evictionListener = evictionListener;
    }


    public Object get(RelationCacheKey key)
    {
        return ((Map)((Map<String, Map>)((Map<String, Map<String, Map>>)this.threeLayersCache
                        .computeIfAbsent(key.getTenantId(), tenant -> new ConcurrentHashMap<>()))
                        .computeIfAbsent(key.getRelationId(), relId -> new ConcurrentHashMap<>()))
                        .computeIfAbsent(key.getRelationSideId(), relSideId -> newConcurrentLinkedHashMap(this.configuration.getCapacityForRelation(key.getRelationId()))))
                        .get(key);
    }


    public void put(RelationCacheKey key, Object loaded)
    {
        ((Map<RelationCacheKey, Object>)((Map<String, Map<RelationCacheKey, Object>>)((Map<String, Map<String, Map<RelationCacheKey, Object>>>)this.threeLayersCache
                        .computeIfAbsent(key.getTenantId(), tenant -> new ConcurrentHashMap<>()))
                        .computeIfAbsent(key.getRelationId(), relId -> new ConcurrentHashMap<>()))
                        .computeIfAbsent(key.getRelationSideId(), relSideId -> newConcurrentLinkedHashMap(this.configuration.getCapacityForRelation(key.getRelationId()))))
                        .put(key, loaded);
    }


    public void clear()
    {
        this.threeLayersCache.clear();
    }


    public Collection<CacheKey> getAllKeys()
    {
        return (Collection<CacheKey>)this.threeLayersCache.values().stream()
                        .flatMap(layer2 -> layer2.values().stream())
                        .flatMap(layer3 -> layer3.values().stream())
                        .flatMap(layer4 -> layer4.keySet().stream())
                        .collect(Collectors.toList());
    }


    public Object remove(RelationCacheKey key)
    {
        if(key.isForRelation())
        {
            return removeRelation(key);
        }
        return ((Map)((Map<String, Map>)((Map<String, Map<String, Map>>)this.threeLayersCache
                        .computeIfAbsent(key.getTenantId(), tenant -> new ConcurrentHashMap<>()))
                        .computeIfAbsent(key.getRelationId(), relId -> new ConcurrentHashMap<>()))
                        .computeIfAbsent(key.getRelationSideId(), relSideId -> newConcurrentLinkedHashMap(this.configuration.getCapacityForRelation(key.getRelationId()))))
                        .remove(key);
    }


    public Object removeRelation(RelationCacheKey key)
    {
        return ((Map)this.threeLayersCache
                        .computeIfAbsent(key.getTenantId(), tenant -> new ConcurrentHashMap<>()))
                        .put(key.getRelationId(), new ConcurrentHashMap<>());
    }


    public boolean containsKey(RelationCacheKey key)
    {
        return ((Map)((Map<String, Map>)((Map<String, Map<String, Map>>)this.threeLayersCache
                        .computeIfAbsent(key.getTenantId(), tenant -> new ConcurrentHashMap<>()))
                        .computeIfAbsent(key.getRelationId(), relId -> new ConcurrentHashMap<>()))
                        .computeIfAbsent(key.getRelationSideId(), relSideId -> newConcurrentLinkedHashMap(this.configuration.getCapacityForRelation(key.getRelationId()))))
                        .containsKey(key);
    }


    private Map<RelationCacheKey, Object> newConcurrentLinkedHashMap(long capacity)
    {
        return (Map<RelationCacheKey, Object>)(new ConcurrentLinkedHashMap.Builder())
                        .maximumWeightedCapacity(capacity)
                        .listener(this.evictionListener)
                        .build();
    }


    Map<String, Map<String, Map<String, Map<RelationCacheKey, Object>>>> getThreeLayersCache()
    {
        return this.threeLayersCache;
    }
}
