package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import java.util.Collection;

public interface FieldNameProvider
{
    Collection<String> getFieldNames(IndexedProperty paramIndexedProperty, String paramString);


    String getFieldName(SolrIndexedPropertyModel paramSolrIndexedPropertyModel, String paramString, FieldType paramFieldType) throws FacetConfigServiceException;


    String getFieldName(IndexedProperty paramIndexedProperty, String paramString, FieldType paramFieldType);


    String getPropertyName(String paramString);
}
