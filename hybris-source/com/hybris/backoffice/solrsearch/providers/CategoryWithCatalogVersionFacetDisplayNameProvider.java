package com.hybris.backoffice.solrsearch.providers;

import com.hybris.backoffice.proxy.LabelServiceProxy;
import com.hybris.backoffice.search.utils.CategoryCatalogVersionMapper;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.solrfacetsearch.provider.impl.CategoryFacetDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CategoryWithCatalogVersionFacetDisplayNameProvider extends CategoryFacetDisplayNameProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(CategoryWithCatalogVersionFacetDisplayNameProvider.class);
    private CategoryCatalogVersionMapper categoryCatalogVersionMapper;
    private CatalogVersionService catalogVersionService;
    private LabelServiceProxy labelServiceProxy;


    public String getDisplayName(SearchQuery query, String name)
    {
        try
        {
            return this.labelServiceProxy.getObjectLabel(getEncodedCategory(name), getLocale(query.getLanguage()));
        }
        catch(RuntimeException e)
        {
            LOG.error("Getting label for facet name failed. Fallback label will be displayed.", e);
            return name;
        }
    }


    protected CategoryModel getEncodedCategory(String code)
    {
        CategoryCatalogVersionMapper.CategoryWithCatalogVersion decoded;
        try
        {
            decoded = this.categoryCatalogVersionMapper.decode(code);
        }
        catch(IllegalArgumentException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Can't decode category: " + code, e);
            }
            return getCategory(code);
        }
        List<CatalogVersionModel> catalogVersions = Collections.singletonList(this.catalogVersionService.getCatalogVersion(decoded.catalogId, decoded.catalogVersion));
        return getCategoryForCatalogVersions(catalogVersions, decoded.categoryCode);
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


    public CategoryCatalogVersionMapper getCategoryCatalogVersionMapper()
    {
        return this.categoryCatalogVersionMapper;
    }


    @Required
    public void setCategoryCatalogVersionMapper(CategoryCatalogVersionMapper categoryCatalogVersionMapper)
    {
        this.categoryCatalogVersionMapper = categoryCatalogVersionMapper;
    }


    public LabelServiceProxy getLabelServiceProxy()
    {
        return this.labelServiceProxy;
    }


    public void setLabelServiceProxy(LabelServiceProxy labelServiceProxy)
    {
        this.labelServiceProxy = labelServiceProxy;
    }
}
