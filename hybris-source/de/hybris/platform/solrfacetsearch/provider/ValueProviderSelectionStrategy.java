package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.util.Collection;
import java.util.Map;

public interface ValueProviderSelectionStrategy
{
    Object getValueProvider(String paramString);


    String resolveValueProvider(IndexedType paramIndexedType, IndexedProperty paramIndexedProperty);


    Map<String, Collection<IndexedProperty>> resolveValueProviders(IndexedType paramIndexedType, Collection<IndexedProperty> paramCollection);
}
