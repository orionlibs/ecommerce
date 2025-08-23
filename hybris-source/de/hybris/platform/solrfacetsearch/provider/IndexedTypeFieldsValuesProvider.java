package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IndexedTypeFieldsValuesProvider
{
    Collection<FieldValue> getFieldValues(IndexConfig paramIndexConfig, Object paramObject) throws FieldValueProviderException;


    Set<String> getFacets();


    Map<String, String> getFieldNamesMapping();
}
