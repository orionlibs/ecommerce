package com.hybris.backoffice.solrsearch.services;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Deprecated(since = "2105", forRemoval = true)
public interface BackofficeFacetSearchConfigService
{
    FacetSearchConfig getFacetSearchConfig(String paramString) throws FacetConfigServiceException;


    Collection<FacetSearchConfig> getAllMappedFacetSearchConfigs();


    default Collection<ComposedTypeModel> getAllMappedTypes()
    {
        return (Collection<ComposedTypeModel>)getAllMappedFacetSearchConfigs().stream().map(FacetSearchConfig::getIndexConfig).map(IndexConfig::getIndexedTypes)
                        .map(Map::values).flatMap(Collection::stream).map(IndexedType::getComposedType).distinct()
                        .collect(Collectors.toList());
    }


    SolrFacetSearchConfigModel getSolrFacetSearchConfigModel(String paramString) throws FacetConfigServiceException;


    SolrIndexedTypeModel getSolrIndexedType(String paramString);


    IndexedType getIndexedType(FacetSearchConfig paramFacetSearchConfig, String paramString);


    boolean isSolrSearchConfiguredForType(String paramString);


    boolean isBackofficeSolrSearchConfiguredForName(String paramString);
}
