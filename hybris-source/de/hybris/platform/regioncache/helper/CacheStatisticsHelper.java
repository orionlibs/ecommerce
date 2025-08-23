package de.hybris.platform.regioncache.helper;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.regioncache.CacheConfiguration;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.region.CacheRegion;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class CacheStatisticsHelper
{
    private static final Logger LOGGER = Logger.getLogger(CacheStatisticsHelper.class);


    public Collection<CacheRegion> getCacheRegions()
    {
        CacheConfiguration config = (CacheConfiguration)Registry.getApplicationContext().getBean("cacheConfiguration", CacheConfiguration.class);
        return config.getRegions();
    }


    public Map<String, String> getTypeNames()
    {
        Map<String, String> typeNames = new HashMap<>();
        Collection<CacheRegion> regions = getCacheRegions();
        for(CacheRegion cacheRegion : regions)
        {
            CacheStatistics stats = cacheRegion.getCacheRegionStatistics();
            Collection<Object> types = stats.getTypes();
            for(Object type : types)
            {
                if(!typeNames.containsKey(String.valueOf(type)))
                {
                    typeNames.put(String.valueOf(type), getTypeNamesForDeployment(type));
                }
            }
        }
        return typeNames;
    }


    private String getTypeNamesForDeployment(Object typeObj)
    {
        String type = (typeObj == null) ? null : typeObj.toString();
        int typeCode = 0;
        try
        {
            typeCode = Integer.parseInt(type);
        }
        catch(NumberFormatException exp)
        {
            return "[INVALID]";
        }
        StringBuilder out = new StringBuilder();
        TypeManager typeManager = TypeManager.getInstance();
        out.append(getTypeNameForTc(typeCode, typeManager));
        PersistenceManager persistenceManager = Registry.getPersistenceManager();
        ItemDeployment itemDeployment = getItemDeployment(typeCode, persistenceManager);
        Collection<ItemDeployment> subDeployments = null;
        if(itemDeployment != null)
        {
            out.append(" (").append(itemDeployment.getName()).append(")");
            subDeployments = persistenceManager.getAllSubDeployments(itemDeployment);
        }
        if(subDeployments != null && !subDeployments.isEmpty())
        {
            out.append(" - ");
            for(ItemDeployment id : subDeployments)
            {
                out.append(getTypeNameForTc(id.getTypeCode(), typeManager)).append(", ");
            }
        }
        return out.toString();
    }


    protected ItemDeployment getItemDeployment(int typeCode, PersistenceManager persistenceManager)
    {
        try
        {
            return persistenceManager.getItemDeployment(typeCode);
        }
        catch(Throwable t)
        {
            LOGGER.warn("No deployment for type code: " + typeCode);
            return null;
        }
    }


    protected String getTypeNameForTc(int typeCode, TypeManager typeManager)
    {
        try
        {
            return typeManager.getRootComposedType(typeCode).getCode();
        }
        catch(Throwable t)
        {
            try
            {
                return Registry.getPersistenceManager().getItemDeployment(typeCode).getDatabaseTableName();
            }
            catch(Throwable t2)
            {
                LOGGER.warn("No type name for tc: " + typeCode);
                return "INVALID";
            }
        }
    }
}
