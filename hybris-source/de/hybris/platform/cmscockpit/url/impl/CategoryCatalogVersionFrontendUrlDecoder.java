package de.hybris.platform.cmscockpit.url.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class CategoryCatalogVersionFrontendUrlDecoder<CategoryModel> extends BaseFrontendRegexUrlDecoder
{
    private FlexibleSearchService flexibleSearchService;
    private CatalogVersionService catalogVersionService;


    protected CategoryModel translateId(String id)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("catalogVersions", getCatalogVersionService().getSessionCatalogVersions());
        SearchResult<CategoryModel> result = getFlexibleSearchService().search("select {PK} from {Category} where {code} = ?id and {catalogVersion} in (?catalogVersions)", params);
        if(result.getCount() > 0)
        {
            return result.getResult().get(0);
        }
        return null;
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }
}
