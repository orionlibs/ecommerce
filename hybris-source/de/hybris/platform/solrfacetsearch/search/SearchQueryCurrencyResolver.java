package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;

public interface SearchQueryCurrencyResolver
{
    CurrencyModel resolveCurrency(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType);
}
