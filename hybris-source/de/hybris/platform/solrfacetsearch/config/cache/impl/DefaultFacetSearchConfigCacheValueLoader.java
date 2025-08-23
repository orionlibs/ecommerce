package de.hybris.platform.solrfacetsearch.config.cache.impl;

import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFacetSearchConfigCacheValueLoader implements CacheValueLoader<FacetSearchConfig>
{
    private static final Logger LOG = Logger.getLogger(DefaultFacetSearchConfigCacheValueLoader.class);
    private SolrFacetSearchConfigDao solrFacetSearchConfigDao;
    private Converter<SolrFacetSearchConfigModel, FacetSearchConfig> solrFacetSearchConfigConverter;


    public FacetSearchConfig load(CacheKey key)
    {
        if(!(key instanceof FacetSearchConfigCacheKey))
        {
            throw new IllegalArgumentException("Key value should be instance of FacetSearchConfigCacheKey class");
        }
        FacetSearchConfigCacheKey facetSearchConfigKey = (FacetSearchConfigCacheKey)key;
        try
        {
            SolrFacetSearchConfigModel configModel = this.solrFacetSearchConfigDao.findFacetSearchConfigByName(facetSearchConfigKey.getName());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Loading FacetSearchConfig for key : " + key);
            }
            return (FacetSearchConfig)this.solrFacetSearchConfigConverter.convert(configModel);
        }
        catch(UnknownIdentifierException e)
        {
            throw new CacheValueLoadException("No such configuration: " + facetSearchConfigKey.getName(), e);
        }
    }


    public SolrFacetSearchConfigDao getSolrFacetSearchConfigDao()
    {
        return this.solrFacetSearchConfigDao;
    }


    @Required
    public void setSolrFacetSearchConfigDao(SolrFacetSearchConfigDao solrFacetSearchConfigDao)
    {
        this.solrFacetSearchConfigDao = solrFacetSearchConfigDao;
    }


    public Converter<SolrFacetSearchConfigModel, FacetSearchConfig> getSolrFacetSearchConfigConverter()
    {
        return this.solrFacetSearchConfigConverter;
    }


    @Required
    public void setSolrFacetSearchConfigConverter(Converter<SolrFacetSearchConfigModel, FacetSearchConfig> solrFacetSearchConfigConverter)
    {
        this.solrFacetSearchConfigConverter = solrFacetSearchConfigConverter;
    }
}
