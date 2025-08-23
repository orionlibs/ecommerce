package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import java.util.List;

public interface FacetTopValuesProvider
{
    List<FacetValue> getTopValues(IndexedProperty paramIndexedProperty, List<FacetValue> paramList);
}
