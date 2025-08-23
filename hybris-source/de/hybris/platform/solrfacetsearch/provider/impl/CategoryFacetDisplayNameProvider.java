package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.solrfacetsearch.provider.FacetDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryFacetDisplayNameProvider implements FacetDisplayNameProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(CategoryFacetDisplayNameProvider.class);
    private CategoryService categoryService;


    protected Locale getLocale(String isoCode)
    {
        Locale result;
        String[] splittedCode = isoCode.split("_");
        if(splittedCode.length == 1)
        {
            result = new Locale(splittedCode[0]);
        }
        else
        {
            result = new Locale(splittedCode[0], splittedCode[1]);
        }
        return result;
    }


    public String getDisplayName(SearchQuery query, String name)
    {
        Locale locale = getLocale(query.getLanguage());
        CategoryModel category = null;
        if(query.getCatalogVersions() != null)
        {
            category = getCategoryForCatalogVersions(query.getCatalogVersions(), name);
        }
        if(category == null)
        {
            category = getCategory(name);
        }
        return (category != null) ? category.getName(locale) : null;
    }


    public void setCategoryService(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }


    protected CategoryModel getCategoryForCatalogVersions(List<CatalogVersionModel> catalogVersions, String code)
    {
        for(CatalogVersionModel catalogVersion : catalogVersions)
        {
            try
            {
                if(catalogVersion != null)
                {
                    return this.categoryService.getCategoryForCode(catalogVersion, code);
                }
            }
            catch(UnknownIdentifierException uie)
            {
            }
        }
        return null;
    }


    protected CategoryModel getCategory(String code)
    {
        CategoryModel category = null;
        try
        {
            category = this.categoryService.getCategoryForCode(code);
        }
        catch(UnknownIdentifierException e)
        {
            LOG.error(e.getMessage());
        }
        return category;
    }
}
