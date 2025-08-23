package de.hybris.platform.solrfacetsearch.config;

import de.hybris.platform.solrfacetsearch.search.FacetValue;
import java.util.Comparator;

public interface FacetSortProvider
{
    Comparator<FacetValue> getComparatorForTypeAndProperty(IndexedType paramIndexedType, IndexedProperty paramIndexedProperty);
}
