package de.hybris.platform.jalo.flexiblesearch;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.regioncache.key.RegistrableCacheKey;
import de.hybris.platform.spring.TenantNotFoundException;
import de.hybris.platform.util.StandardSearchResult;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;

class FlexibleSearchCacheUnit extends AbstractCacheUnit
{
    private final FlexibleSearch fs;
    private final FlexibleSearch.FlexibleSearchCacheKey cacheKey;
    private final Map values;
    private final int prefetchSize;
    private final Set<PK> prefetchLanguages;
    private final String dataSourceId;
    private final String RR_SUFFIX = "RR";
    private boolean usedCachedResult = true;


    @Deprecated(since = "2105", forRemoval = true) FlexibleSearchCacheUnit(FlexibleSearch fs, Map values, FlexibleSearch.FlexibleSearchCacheKey cacheKey, int prefetchSize, Set<PK> prefetchLanguages, Cache cache)
    {
        this(fs, values, cacheKey, prefetchSize, prefetchLanguages, cache, null);
    }


    FlexibleSearchCacheUnit(FlexibleSearch fs, Map values, FlexibleSearch.FlexibleSearchCacheKey cacheKey, int prefetchSize, Set<PK> prefetchLanguages, Cache cache, String dataSourceId)
    {
        super(cache);
        this.fs = fs;
        this.cacheKey = cacheKey;
        this.values = values;
        this.prefetchSize = prefetchSize;
        this.prefetchLanguages = prefetchLanguages;
        this.dataSourceId = dataSourceId;
    }


    public final Object compute() throws FlexibleSearchException
    {
        StandardSearchResult ret;
        this.usedCachedResult = false;
        Optional<? extends DataSource> dataSourceById = getDataSourceById(this.dataSourceId, this.cacheKey.getTenantId());
        if(dataSourceById.isEmpty())
        {
            ret = (StandardSearchResult)this.fs.executeSearch(this.cacheKey.tq, this.values, this.cacheKey.langPK,
                            Arrays.asList((Class<?>[][])this.cacheKey.resultClasses), this.cacheKey
                                            .hasModifier(1), this.cacheKey.start, this.cacheKey.count, this.prefetchSize, this.prefetchLanguages, this.cacheKey
                                            .hasModifier(2), this.cacheKey.hints);
        }
        else
        {
            ret = (StandardSearchResult)this.fs.executeSearch(this.cacheKey.tq, this.values, this.cacheKey.langPK,
                            Arrays.asList((Class<?>[][])this.cacheKey.resultClasses), this.cacheKey
                                            .hasModifier(1), this.cacheKey.start, this.cacheKey.count, this.prefetchSize, this.prefetchLanguages, this.cacheKey
                                            .hasModifier(2), this.cacheKey.hints, dataSourceById
                                            .get());
        }
        ret.setUnitHash(System.identityHashCode(this));
        return ret;
    }


    protected Optional<? extends DataSource> getDataSourceById(String dataSourceId, String tenantId)
    {
        Tenant tenant = Registry.getTenantByID(tenantId);
        if(tenant == null)
        {
            if(tenantId.endsWith("RR"))
            {
                getDataSourceById(dataSourceId, StringUtils.removeEnd(tenantId, "RR"));
            }
            else
            {
                throw new TenantNotFoundException(String.format("Tenant with id: %s not found", new Object[] {tenantId}));
            }
        }
        else
        {
            if(tenant.getAllAlternativeMasterDataSourceIDs().contains(dataSourceId))
            {
                return tenant.getAllAlternativeMasterDataSources()
                                .stream()
                                .filter(d -> d.getID().equals(dataSourceId))
                                .findFirst();
            }
            if(tenant.getAllSlaveDataSourceIDs().contains(dataSourceId))
            {
                return tenant.getAllSlaveDataSources().stream().filter(d -> d.getID().equals(dataSourceId)).findFirst();
            }
        }
        return Optional.empty();
    }


    public Object[] createKey()
    {
        if((this.cacheKey.getDependentTypes()).length > 1)
        {
            Object[] multiKey = new Object[(this.cacheKey.getDependentTypes()).length];
            int i = 0;
            for(String tc : this.cacheKey.getDependentTypes())
            {
                (new Object[3])[0] = Cache.CACHEKEY_FLEXSEARCH;
                (new Object[3])[1] = tc;
                (new Object[3])[2] = this.cacheKey;
                multiKey[i++] = new Object[3];
            }
            return multiKey;
        }
        return new Object[] {Cache.CACHEKEY_FLEXSEARCH, this.cacheKey
                        .getDependentTypes()[0], this.cacheKey};
    }


    public final StandardSearchResult myGet() throws Exception
    {
        StandardSearchResult ret = (StandardSearchResult)get();
        ret.setFromCache(this.usedCachedResult);
        return ret;
    }


    public void removedFromCache()
    {
        super.removedFromCache();
    }


    public RegistrableCacheKey getKey()
    {
        return (RegistrableCacheKey)this.cacheKey;
    }
}
