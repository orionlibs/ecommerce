package de.hybris.platform.directpersistence.cache;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.read.SLDItemDAO;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSLDDataContainerProvider implements SLDDataContainerProvider
{
    private static final Logger LOG = Logger.getLogger(DefaultSLDDataContainerProvider.class);
    public static final String SLD_CACHE_KEY = "__SLD_CACHE__";
    private SLDItemDAO sldItemDAO;


    public SLDDataContainer get(PK pk)
    {
        SLDItemLoadUnit cacheUnit = new SLDItemLoadUnit(getCache(), this.sldItemDAO, pk);
        try
        {
            return (SLDDataContainer)cacheUnit.get();
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e);
        }
    }


    public List<SLDDataContainer> getAll(List<PK> pks)
    {
        preload(pks);
        return (List<SLDDataContainer>)pks.stream().map(this::toSLDDataContainerOrNull).collect(Collectors.toList());
    }


    public void preload(List<PK> pks)
    {
        Cache cache = getCache();
        SLDBulkCacheLoader bulkLoader = new SLDBulkCacheLoader(this.sldItemDAO, pks);
        try
        {
            pks.stream().filter(pk -> (pk != null)).forEach(pk -> putInCache(cache, bulkLoader, pk));
        }
        finally
        {
            bulkLoader.release();
        }
    }


    private SLDDataContainer toSLDDataContainerOrNull(PK pk)
    {
        return (pk == null) ? null : get(pk);
    }


    private void putInCache(Cache cache, SLDBulkCacheLoader bulkLoader, PK pk)
    {
        try
        {
            cache.getOrAddUnit((AbstractCacheUnit)new SLDItemBulkLoadUnit(cache, bulkLoader, pk));
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e);
        }
    }


    Cache getCache()
    {
        return Registry.getCurrentTenant().getCache();
    }


    @Required
    public void setSldItemDAO(SLDItemDAO sldItemDAO)
    {
        this.sldItemDAO = sldItemDAO;
    }
}
