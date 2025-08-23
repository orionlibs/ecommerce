package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.cache.FacetSearchConfigCacheService;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFacetSearchConfigService implements FacetSearchConfigService
{
    private static final Logger LOG = Logger.getLogger(DefaultFacetSearchConfigService.class);
    private FacetSearchConfigCacheService facetSearchConfigCacheService;
    private ConfigurableMapper facetSearchConfigMapper;


    public FacetSearchConfig getConfiguration(String name) throws FacetConfigServiceException
    {
        try
        {
            FacetSearchConfig facetSearchConfig = this.facetSearchConfigCacheService.putOrGetFromCache(name);
            FacetSearchConfig copyOfFacetSearchConfig = (FacetSearchConfig)this.facetSearchConfigMapper.map(facetSearchConfig, FacetSearchConfig.class);
            return copyOfFacetSearchConfig;
        }
        catch(CacheValueLoadException e)
        {
            throw new FacetConfigServiceException(name, "Get configuration error", e);
        }
    }


    public FacetSearchConfig getConfiguration(CatalogVersionModel catalogVersion) throws FacetConfigServiceException
    {
        List<SolrFacetSearchConfigModel> configs = catalogVersion.getFacetSearchConfigs();
        if(configs.isEmpty())
        {
            LOG.warn("no solr configuration can be found for catalog version [" + catalogVersion.getCatalog().getId() + catalogVersion
                            .getVersion() + "]");
            return null;
        }
        if(configs.size() > 1)
        {
            LOG.warn("more than one solr configuration can be found for catalog version [" + catalogVersion.getCatalog().getId() + catalogVersion
                            .getVersion() + "], and the first one [" + ((SolrFacetSearchConfigModel)configs.get(0)).getName() + "] is taken.");
        }
        return getConfiguration(((SolrFacetSearchConfigModel)configs.get(0)).getName());
    }


    public IndexedType resolveIndexedType(FacetSearchConfig facetSearchConfig, String indexedTypeName) throws FacetConfigServiceException
    {
        IndexedType indexedType = null;
        IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
        if(indexConfig.getIndexedTypes() != null)
        {
            indexedType = (IndexedType)indexConfig.getIndexedTypes().get(indexedTypeName);
        }
        if(indexedType == null)
        {
            throw new FacetConfigServiceException("Indexed type \"" + indexedTypeName + "\" not found");
        }
        return indexedType;
    }


    public List<IndexedProperty> resolveIndexedProperties(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Collection<String> indexedPropertiesIds) throws FacetConfigServiceException
    {
        List<IndexedProperty> indexedProperties = new ArrayList<>();
        for(String indexedPropertyId : indexedPropertiesIds)
        {
            IndexedProperty indexedProperty = null;
            if(indexedType.getIndexedProperties() != null)
            {
                indexedProperty = (IndexedProperty)indexedType.getIndexedProperties().get(indexedPropertyId);
            }
            if(indexedProperty == null)
            {
                throw new FacetConfigServiceException("Indexed property \"" + indexedPropertyId + "\" not found");
            }
            indexedProperties.add(indexedProperty);
        }
        return indexedProperties;
    }


    public FacetSearchConfigCacheService getFacetSearchConfigCacheService()
    {
        return this.facetSearchConfigCacheService;
    }


    @Required
    public void setFacetSearchConfigCacheService(FacetSearchConfigCacheService facetSearchConfigCacheService)
    {
        this.facetSearchConfigCacheService = facetSearchConfigCacheService;
    }


    @Required
    public void setFacetSearchConfigMapper(ConfigurableMapper facetSearchConfigMapper)
    {
        this.facetSearchConfigMapper = facetSearchConfigMapper;
    }
}
