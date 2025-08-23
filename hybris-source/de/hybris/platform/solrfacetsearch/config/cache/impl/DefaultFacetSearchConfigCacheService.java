package de.hybris.platform.solrfacetsearch.config.cache.impl;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.cache.FacetSearchConfigCacheService;
import de.hybris.platform.solrfacetsearch.config.cache.FacetSearchConfigInvalidationTypeSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultFacetSearchConfigCacheService implements FacetSearchConfigCacheService, ApplicationContextAware
{
    private static final Logger LOG = Logger.getLogger(DefaultFacetSearchConfigCacheService.class);
    private Set<String> invalidationTypes;
    private CacheController cacheController;
    private FacetSearchConfigCacheRegion facetSearchConfigCacheRegion;
    private DefaultFacetSearchConfigCacheValueLoader facetSearchConfigCacheValueLoader;
    private CommonI18NService commonI18NService;
    private String tenantId;
    private ApplicationContext applicationContext;
    private final InvalidationListener invalidationListener = (InvalidationListener)new Object(this);


    @PostConstruct
    public void init()
    {
        this.tenantId = Registry.getCurrentTenantNoFallback().getTenantID();
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener(this.invalidationListener);
        createInvalidationTypeSet();
    }


    protected void createInvalidationTypeSet()
    {
        this.invalidationTypes = new HashSet<>();
        Map<String, FacetSearchConfigInvalidationTypeSet> beanMap = this.applicationContext.getBeansOfType(FacetSearchConfigInvalidationTypeSet.class);
        for(FacetSearchConfigInvalidationTypeSet invalidationTypesBean : beanMap.values())
        {
            this.invalidationTypes.addAll(invalidationTypesBean.getInvalidationTypes());
        }
    }


    public FacetSearchConfig putOrGetFromCache(String configName)
    {
        return (FacetSearchConfig)this.cacheController.getWithLoader((CacheKey)createCacheKey(configName), (CacheValueLoader)this.facetSearchConfigCacheValueLoader);
    }


    public void invalidate(String name)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Invalidating facet search config cache for : " + name);
        }
        List<CacheKey> keyList = this.facetSearchConfigCacheRegion.findCachedObjectKeys(name, this.tenantId);
        for(CacheKey key : keyList)
        {
            this.cacheController.invalidate(key);
        }
    }


    protected FacetSearchConfigCacheKey createCacheKey(String configName)
    {
        FacetSearchConfigCacheKey key = new FacetSearchConfigCacheKey(configName, getLanguage(), this.tenantId);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Created key :" + key);
        }
        return key;
    }


    protected String getLanguage()
    {
        LanguageModel language = this.commonI18NService.getCurrentLanguage();
        return (language == null) ? null : language.getIsocode();
    }


    @Required
    public void setCacheController(CacheController cacheController)
    {
        this.cacheController = cacheController;
    }


    public DefaultFacetSearchConfigCacheValueLoader getFacetSearchConfigCacheValueLoader()
    {
        return this.facetSearchConfigCacheValueLoader;
    }


    @Required
    public void setFacetSearchConfigCacheValueLoader(DefaultFacetSearchConfigCacheValueLoader facetSearchConfigCacheValueLoader)
    {
        this.facetSearchConfigCacheValueLoader = facetSearchConfigCacheValueLoader;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public FacetSearchConfigCacheRegion getFacetSearchConfigCacheRegion()
    {
        return this.facetSearchConfigCacheRegion;
    }


    @Required
    public void setFacetSearchConfigCacheRegion(FacetSearchConfigCacheRegion facetSearchConfigCacheRegion)
    {
        this.facetSearchConfigCacheRegion = facetSearchConfigCacheRegion;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
