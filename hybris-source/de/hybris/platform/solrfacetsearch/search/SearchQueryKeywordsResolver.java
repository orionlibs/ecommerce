package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.util.List;

public interface SearchQueryKeywordsResolver
{
    List<Keyword> resolveKeywords(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, String paramString);
}
