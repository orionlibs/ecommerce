package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQueryCatalogVersionsResolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSearchQueryCatalogVersionsResolver implements SearchQueryCatalogVersionsResolver
{
    private CatalogVersionService catalogVersionService;


    public List<CatalogVersionModel> resolveCatalogVersions(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        ServicesUtil.validateParameterNotNull(facetSearchConfig, "FacetSearchConfig cannot be null");
        Collection<CatalogVersionModel> configuredCatalogVersions = facetSearchConfig.getIndexConfig().getCatalogVersions();
        List<CatalogVersionModel> result = new ArrayList<>();
        if(configuredCatalogVersions != null && !configuredCatalogVersions.isEmpty())
        {
            for(CatalogVersionModel catalogVersion : this.catalogVersionService.getSessionCatalogVersions())
            {
                if(configuredCatalogVersions.contains(catalogVersion))
                {
                    result.add(catalogVersion);
                }
            }
        }
        return result;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }
}
