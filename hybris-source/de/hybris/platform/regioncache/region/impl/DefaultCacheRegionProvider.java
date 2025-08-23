package de.hybris.platform.regioncache.region.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.regioncache.CacheRegionsHolder;
import de.hybris.platform.regioncache.RegionRegistryAllocationStrategy;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionProvider;
import de.hybris.platform.regioncache.region.RegionType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class DefaultCacheRegionProvider implements CacheRegionProvider
{
    private static final Logger LOGGER = Logger.getLogger(DefaultCacheRegionProvider.class);
    private RegionRegistryAllocationStrategy regionRegistryAllocationStrategy;
    final Collection<CacheRegion> _cacheRegions;
    protected List<CacheRegion> allRegions;
    protected Map<String, CacheRegion> manualRegions;
    protected List<CacheRegion> regularRegions;
    protected CacheRegion allTypeRegion;
    protected List<CacheRegion> queryTypeRegions;
    protected Map<String, List<CacheRegion>> regionsTypes;
    protected Map<String, List<CacheRegion>> regionsSingletonCollections;


    public DefaultCacheRegionProvider(CacheRegionsHolder cacheRegionsHolder)
    {
        this(cacheRegionsHolder.getRegions());
    }


    public DefaultCacheRegionProvider(Collection<CacheRegion> cacheRegions)
    {
        this._cacheRegions = cacheRegions;
    }


    protected Collection<CacheRegion> createDefaultRegion()
    {
        return (Collection)Collections.singletonList(new EHCacheRegion("default", 50000, "LRU", false, true));
    }


    @PostConstruct
    public void init()
    {
        Collection<CacheRegion> cacheRegions;
        if(this._cacheRegions == null || this._cacheRegions.size() < 1)
        {
            LOGGER.warn("No region definition found creating default region");
            cacheRegions = createDefaultRegion();
        }
        else
        {
            cacheRegions = this._cacheRegions;
        }
        List<CacheRegion> allRegions_ = new ArrayList<>(5);
        Map<String, List<CacheRegion>> regionsSingletonCollections_ = new HashMap<>();
        Map<String, CacheRegion> manualRegions_ = new HashMap<>();
        List<CacheRegion> regularRegions_ = new ArrayList<>(5);
        Map<String, CacheRegion> allTypeRegions_ = new HashMap<>();
        List<CacheRegion> queryTypeRegions_ = new ArrayList<>(5);
        Map<String, List<CacheRegion>> regionsTypes_ = new HashMap<>();
        for(CacheRegion cacheRegion : cacheRegions)
        {
            if(cacheRegion == null)
            {
                throw new IllegalArgumentException("NULL region found in configuration.");
            }
            allRegions_.add(cacheRegion);
            regionsSingletonCollections_.put(cacheRegion.getName(), Collections.singletonList(cacheRegion));
            if(isManualRegion(cacheRegion))
            {
                manualRegions_.put(cacheRegion.getName(), cacheRegion);
            }
            else
            {
                regularRegions_.add(cacheRegion);
                if(this.regionRegistryAllocationStrategy.isRegionRequiresRegistry(cacheRegion))
                {
                    queryTypeRegions_.add(cacheRegion);
                }
            }
            String[] types = cacheRegion.getHandledTypes();
            if(types != null)
            {
                for(String type : types)
                {
                    if(type != null && !type.isEmpty())
                    {
                        if(RegionType.ALL_TYPES.value().equals(type))
                        {
                            allTypeRegions_.put(cacheRegion.getName(), cacheRegion);
                        }
                        else
                        {
                            List<CacheRegion> regionMap = regionsTypes_.get(type);
                            if(regionMap == null)
                            {
                                regionsTypes_.put(type, regionMap = new ArrayList<>(5));
                            }
                            regionMap.add(cacheRegion);
                        }
                    }
                }
            }
        }
        Map<String, List<CacheRegion>> regionsTypesTmp_ = new HashMap<>();
        Set<Map.Entry<String, List<CacheRegion>>> entrySet = regionsTypes_.entrySet();
        for(Map.Entry<String, List<CacheRegion>> entry : entrySet)
        {
            regionsTypesTmp_.put(entry.getKey(), entry.getValue());
        }
        regionsTypes_ = regionsTypesTmp_;
        if(allTypeRegions_.size() > 1)
        {
            throw new IllegalArgumentException("Configuration error expected at most one ALL_TYPES region. Found " + allTypeRegions_
                            .size());
        }
        this.allRegions = (List<CacheRegion>)ImmutableList.copyOf(allRegions_);
        this.regionsSingletonCollections = (Map<String, List<CacheRegion>>)ImmutableMap.copyOf(regionsSingletonCollections_);
        this.manualRegions = (Map<String, CacheRegion>)ImmutableMap.copyOf(manualRegions_);
        this.regularRegions = (List<CacheRegion>)ImmutableList.copyOf(regularRegions_);
        this.allTypeRegion = allTypeRegions_.isEmpty() ? null : allTypeRegions_.values().iterator().next();
        this.regionsTypes = regionsTypes_;
        this.queryTypeRegions = (List<CacheRegion>)ImmutableList.copyOf(queryTypeRegions_);
    }


    public List<CacheRegion> getAllRegions()
    {
        return this.allRegions;
    }


    public List<CacheRegion> getRegions()
    {
        return this.regularRegions;
    }


    public List<CacheRegion> getRegionsForType(Object typeCode)
    {
        return this.regionsTypes.get(String.valueOf(typeCode));
    }


    public CacheRegion getRegionForAllTypes()
    {
        return this.allTypeRegion;
    }


    public List<CacheRegion> getRegionByName(String name)
    {
        return this.regionsSingletonCollections.get(name);
    }


    public List<CacheRegion> getQueryRegions()
    {
        return this.queryTypeRegions;
    }


    public CacheRegion getManualRegion(String name)
    {
        if(name == null)
        {
            return null;
        }
        return this.manualRegions.get(name);
    }


    public Collection<CacheRegion> getManualRegions()
    {
        return this.manualRegions.values();
    }


    protected boolean isManualRegion(CacheRegion region)
    {
        if(region == null)
        {
            return false;
        }
        String[] types = region.getHandledTypes();
        if(types == null || types.length < 1 || (types.length == 1 && RegionType.NON_REGISTRABLE.value().equals(types[0])))
        {
            return true;
        }
        return false;
    }


    @Resource(name = "regionRegistryAllocationStrategy")
    public void setRegionRegistryAllocationStrategy(RegionRegistryAllocationStrategy regionRegistryAllocationStrategy)
    {
        this.regionRegistryAllocationStrategy = regionRegistryAllocationStrategy;
    }
}
