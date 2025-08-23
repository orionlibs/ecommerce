package de.hybris.platform.solrfacetsearch.config;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import java.util.Collection;
import java.util.List;

public interface FacetSearchConfigService
{
    FacetSearchConfig getConfiguration(String paramString) throws FacetConfigServiceException;


    FacetSearchConfig getConfiguration(CatalogVersionModel paramCatalogVersionModel) throws FacetConfigServiceException;


    IndexedType resolveIndexedType(FacetSearchConfig paramFacetSearchConfig, String paramString) throws FacetConfigServiceException;


    List<IndexedProperty> resolveIndexedProperties(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Collection<String> paramCollection) throws FacetConfigServiceException;
}
