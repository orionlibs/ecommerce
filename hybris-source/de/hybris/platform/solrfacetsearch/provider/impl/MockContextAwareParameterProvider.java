package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.ContextAwareParameterProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MockContextAwareParameterProvider implements ContextAwareParameterProvider
{
    public Map<String, Object> createParameters(IndexConfig indexConfig, IndexedType indexedType)
    {
        Map<String, Object> result = null;
        int counter = 0;
        for(CatalogVersionModel catVer : indexConfig.getCatalogVersions())
        {
            if(result == null)
            {
                result = new HashMap<>();
            }
            result.put("catalogVersion" + ++counter, catVer.getVersion());
        }
        return (result == null) ? Collections.<String, Object>emptyMap() : result;
    }
}
