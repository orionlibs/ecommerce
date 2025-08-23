package de.hybris.platform.adaptivesearchsolr.strategies.impl;

import de.hybris.platform.adaptivesearchsolr.strategies.SolrAsCatalogVersionResolver;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class DefaultSolrAsCatalogVersionResolver implements SolrAsCatalogVersionResolver
{
    public List<CatalogVersionModel> resolveCatalogVersions(SearchQuery searchQuery)
    {
        if(CollectionUtils.isEmpty(searchQuery.getCatalogVersions()))
        {
            return Collections.emptyList();
        }
        return (List<CatalogVersionModel>)searchQuery.getCatalogVersions().stream().filter(this::isSupportedCatalogVersion).collect(Collectors.toList());
    }


    protected boolean isSupportedCatalogVersion(CatalogVersionModel catalogVersion)
    {
        return (catalogVersion != null && !(catalogVersion instanceof de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel));
    }
}
