package de.hybris.platform.adaptivesearch.searchservices.strategies;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.searchservices.search.service.SnSearchContext;
import java.util.List;

@FunctionalInterface
public interface SnAsCatalogVersionResolver
{
    List<CatalogVersionModel> resolveCatalogVersions(SnSearchContext paramSnSearchContext);
}
