package de.hybris.platform.hac.facade;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.CacheStatisticsEntry;
import de.hybris.platform.cache.impl.CacheFactory;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Registry;
import de.hybris.platform.hac.data.dto.cache.legacy.LegacyCacheData;
import de.hybris.platform.hac.data.dto.cache.region.CacheStatisticsData;
import de.hybris.platform.hac.data.dto.cache.region.RegionCacheData;
import de.hybris.platform.hac.data.dto.cache.region.RegionData;
import de.hybris.platform.hac.data.dto.cache.region.TypeStatisticsData;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.regioncache.CacheConfiguration;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.region.CacheRegion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class HacCacheFacade
{
    private static final Logger LOG = Logger.getLogger(HacCacheFacade.class);
    private static final String INVALID_TYPENAME = "INVALID";
    private CacheConfiguration cacheConfiguration;


    public boolean isRegionCacheEnabled()
    {
        return !CacheFactory.isLegacyMode(Registry.getCurrentTenant());
    }


    public List<CacheRegion> getCacheRegions()
    {
        return this.cacheConfiguration.getRegions();
    }


    public RegionData getCacheRegion(String name)
    {
        Optional<RegionData> found = getRegionCacheData().getRegions().stream().filter(r -> r.getName().equals(name)).findFirst();
        return found.orElse(null);
    }


    public RegionCacheData getRegionCacheData()
    {
        RegionCacheData regionCacheData = new RegionCacheData();
        List<CacheRegion> regions = getCacheRegions();
        regionCacheData.setTotalRegionCount(regions.size());
        regionCacheData.setRegions(buildRegionsData(regions));
        return regionCacheData;
    }


    private List<RegionData> buildRegionsData(List<CacheRegion> regions)
    {
        List<RegionData> result = new ArrayList<>();
        for(CacheRegion cacheRegion : regions)
        {
            RegionData regionData = new RegionData();
            regionData.setName(cacheRegion.getName());
            regionData.setMaxEntries(cacheRegion.getCacheMaxEntries());
            regionData.setMaxReachedSize(cacheRegion.getMaxReachedSize());
            CacheStatistics cacheStatistics = cacheRegion.getCacheRegionStatistics();
            regionData.setFactor(cacheStatistics.getFactor());
            regionData.setCacheStatistics(buildCacheStatistics(cacheStatistics));
            regionData.setTypesStatistics(buildStatisticsForTypes(cacheStatistics));
            result.add(regionData);
        }
        return result;
    }


    private CacheStatisticsData buildCacheStatistics(CacheStatistics cacheStatistics)
    {
        CacheStatisticsData cacheStatisticsData = new CacheStatisticsData();
        cacheStatisticsData.setHits(cacheStatistics.getHits());
        cacheStatisticsData.setFetches(cacheStatistics.getFetches());
        cacheStatisticsData.setMisses(cacheStatistics.getMisses());
        cacheStatisticsData.setEvictions(cacheStatistics.getEvictions());
        cacheStatisticsData.setInvalidations(cacheStatistics.getInvalidations());
        cacheStatisticsData.setInstanceCount(cacheStatistics.getInstanceCount());
        return cacheStatisticsData;
    }


    private List<TypeStatisticsData> buildStatisticsForTypes(CacheStatistics cacheStatistics)
    {
        List<TypeStatisticsData> result = new ArrayList<>();
        Collection<Object> types = cacheStatistics.getTypes();
        for(Object type : types)
        {
            TypeStatisticsData typeStatisticsData = new TypeStatisticsData();
            typeStatisticsData.setType(type);
            typeStatisticsData.setTypeName(getTypeNamesForDeployment(type));
            typeStatisticsData.setHits(cacheStatistics.getHits(type));
            typeStatisticsData.setFetches(cacheStatistics.getFetches(type));
            typeStatisticsData.setMisses(cacheStatistics.getMisses(type));
            typeStatisticsData.setEvictions(cacheStatistics.getEvictions(type));
            typeStatisticsData.setInvalidations(cacheStatistics.getInvalidations(type));
            result.add(typeStatisticsData);
        }
        return result;
    }


    public String getTypeNamesForDeployment(Object typeObj)
    {
        String result, type = (typeObj == null) ? null : typeObj.toString();
        try
        {
            result = formatValidNameForTypeCode(Integer.parseInt(type));
        }
        catch(NumberFormatException e)
        {
            result = "INVALID";
        }
        return result;
    }


    private String formatValidNameForTypeCode(int typeCode)
    {
        StringBuilder builder = new StringBuilder(getTypeNameForTypeCode(typeCode));
        PersistenceManager persistenceManager = Registry.getPersistenceManager();
        ItemDeployment itemDeployment = getItemDeployment(typeCode, persistenceManager);
        Collection<ItemDeployment> subDeployments = null;
        if(itemDeployment != null)
        {
            builder.append(" (").append(itemDeployment.getName().replaceAll("\\.", ". ")).append(")");
            subDeployments = persistenceManager.getAllSubDeployments(itemDeployment);
        }
        if(CollectionUtils.isNotEmpty(subDeployments))
        {
            builder.append(" - ");
            for(ItemDeployment id : subDeployments)
            {
                builder.append(getTypeNameForTypeCode(id.getTypeCode())).append(", ");
            }
        }
        return builder.toString();
    }


    private ItemDeployment getItemDeployment(int typeCode, PersistenceManager persistenceManager)
    {
        try
        {
            return persistenceManager.getItemDeployment(typeCode);
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No deployment for type code: " + typeCode, e);
            }
            return null;
        }
    }


    private String getTypeNameForTypeCode(int typeCode)
    {
        String result;
        TypeManager typeManager = TypeManager.getInstance();
        try
        {
            result = typeManager.getRootComposedType(typeCode).getCode();
        }
        catch(Exception e)
        {
            result = getFallbackTypeNameForTypeCode(typeCode);
        }
        return result;
    }


    private String getFallbackTypeNameForTypeCode(int typeCode)
    {
        String result;
        try
        {
            ItemDeployment itemDeployment = Registry.getPersistenceManager().getItemDeployment(typeCode);
            if(itemDeployment == null)
            {
                result = "INVALID";
            }
            else
            {
                result = itemDeployment.getDatabaseTableName();
            }
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No type name for type code: " + typeCode, e);
            }
            result = "INVALID";
        }
        return result;
    }


    public LegacyCacheData getLegacyCacheData()
    {
        Cache cache = getCurrentCache();
        LegacyCacheData result = new LegacyCacheData();
        result.setMaxSize(cache.getMaxAllowedSize());
        result.setCurrentSize(cache.getSize());
        result.setMaxReachedSize(cache.getMaxReachedSize());
        result.setNumHitsSinceStart(cache.getGetCount());
        result.setNumAddsSinceStart(cache.getAddCount());
        result.setNumDeletesSinceStart(cache.getRemoveCount());
        result.setNumMissedSinceStart(cache.getMissCount());
        return result;
    }


    public Set<? extends CacheStatisticsEntry> getLegacyCacheStatistics(int upperBound, int lowerBound)
    {
        Set<? extends CacheStatisticsEntry> statistics = getCurrentCache().getStatistics(upperBound, lowerBound);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Stats( " + lowerBound + "," + upperBound + ") contains " + statistics.size() + " entries.");
        }
        return statistics;
    }


    public void toggleLegacyCacheStatistics(boolean enable)
    {
        getCurrentCache().enableStats(enable);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Stats enabled? " + enable);
        }
    }


    public void clearCache()
    {
        getCurrentCache().clear();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Stats cleared.");
        }
    }


    private Cache getCurrentCache()
    {
        return Registry.getCurrentTenant().getCache();
    }


    @Required
    public void setCacheConfiguration(CacheConfiguration cacheConfiguration)
    {
        this.cacheConfiguration = cacheConfiguration;
    }
}
