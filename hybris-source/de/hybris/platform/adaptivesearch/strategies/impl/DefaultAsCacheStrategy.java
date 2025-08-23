package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.strategies.AsCacheKey;
import de.hybris.platform.adaptivesearch.strategies.AsCacheScope;
import de.hybris.platform.adaptivesearch.strategies.AsCacheStrategy;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import java.util.function.Function;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsCacheStrategy implements AsCacheStrategy, InitializingBean
{
    protected static final String AS_CACHE_ENABLED_KEY = "adaptivesearch.cache.enabled";
    protected static final String AS_LOAD_CACHE_ENABLED_KEY = "adaptivesearch.cache.load.enabled";
    protected static final String AS_CALCULATION_CACHE_ENABLED_KEY = "adaptivesearch.cache.calculation.enabled";
    protected static final String AS_MERGE_CACHE_ENABLED_KEY = "adaptivesearch.cache.merge.enabled";
    private TenantService tenantService;
    private ConfigurationService configurationService;
    private CacheRegion cacheRegion;
    private String tenantId;
    private boolean cacheEnabled;
    private boolean loadCacheEnabled;
    private boolean calculationCacheEnabled;


    public void afterPropertiesSet()
    {
        this.tenantId = this.tenantService.getCurrentTenantId();
        loadCacheSettings();
    }


    protected void loadCacheSettings()
    {
        this.cacheEnabled = this.configurationService.getConfiguration().getBoolean("adaptivesearch.cache.enabled", true);
        this.loadCacheEnabled = this.configurationService.getConfiguration().getBoolean("adaptivesearch.cache.load.enabled", true);
        this.calculationCacheEnabled = this.configurationService.getConfiguration().getBoolean("adaptivesearch.cache.calculation.enabled", true);
    }


    public boolean isEnabled(AsCacheScope cacheScope)
    {
        if(!this.cacheEnabled)
        {
            return false;
        }
        switch(null.$SwitchMap$de$hybris$platform$adaptivesearch$strategies$AsCacheScope[cacheScope.ordinal()])
        {
            case 1:
                return this.loadCacheEnabled;
            case 2:
                return this.calculationCacheEnabled;
        }
        return false;
    }


    public <V> V getWithLoader(AsCacheKey cacheKey, Function<AsCacheKey, V> valueLoader)
    {
        if(isEnabled(cacheKey.getScope()))
        {
            return (V)this.cacheRegion.getWithLoader((CacheKey)new HybrisAsCacheKey(this.tenantId, cacheKey), key -> valueLoader.apply(cacheKey));
        }
        return valueLoader.apply(cacheKey);
    }


    public void clear()
    {
        this.cacheRegion.clearCache();
    }


    public long getSize()
    {
        return this.cacheRegion.getMaxReachedSize();
    }


    public long getHits()
    {
        return this.cacheRegion.getCacheRegionStatistics().getHits();
    }


    public long getMisses()
    {
        return this.cacheRegion.getCacheRegionStatistics().getMisses();
    }


    protected String getTenantId()
    {
        return this.tenantId;
    }


    public TenantService getTenantService()
    {
        return this.tenantService;
    }


    @Required
    public void setTenantService(TenantService tenantService)
    {
        this.tenantService = tenantService;
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public CacheRegion getCacheRegion()
    {
        return this.cacheRegion;
    }


    @Required
    public void setCacheRegion(CacheRegion cacheRegion)
    {
        this.cacheRegion = cacheRegion;
    }
}
