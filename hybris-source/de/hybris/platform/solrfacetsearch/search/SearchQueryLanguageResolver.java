package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;

public interface SearchQueryLanguageResolver
{
    LanguageModel resolveLanguage(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType);
}
