package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.util.Map;

public interface ContextAwareParameterProvider
{
    Map<String, Object> createParameters(IndexConfig paramIndexConfig, IndexedType paramIndexedType);
}
