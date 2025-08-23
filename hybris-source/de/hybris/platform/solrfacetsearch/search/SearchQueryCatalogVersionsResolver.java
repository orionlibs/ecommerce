package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.util.List;

public interface SearchQueryCatalogVersionsResolver
{
    List<CatalogVersionModel> resolveCatalogVersions(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType);
}
