package de.hybris.platform.solrfacetsearch.common;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.util.List;

public interface ListenersFactory
{
    <T> List<T> getListeners(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Class<T> paramClass);
}
