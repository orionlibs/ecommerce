package com.hybris.backoffice.solrsearch.converters;

import com.hybris.cockpitng.search.data.facet.FacetData;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.Breadcrumb;
import de.hybris.platform.solrfacetsearch.search.Facet;
import java.util.Collection;
import java.util.List;

public interface FullTextSearchDataConverter
{
    Collection<FacetData> convertFacets(Collection<Facet> paramCollection, List<Breadcrumb> paramList, IndexedType paramIndexedType);
}
