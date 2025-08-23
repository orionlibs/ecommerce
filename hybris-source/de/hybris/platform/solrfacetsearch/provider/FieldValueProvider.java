package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import java.util.Collection;

@Deprecated(since = "5.5")
public interface FieldValueProvider
{
    Collection<FieldValue> getFieldValues(IndexConfig paramIndexConfig, IndexedProperty paramIndexedProperty, Object paramObject) throws FieldValueProviderException;
}
