package com.hybris.backoffice.searchservices.providers.impl;

import com.hybris.backoffice.proxy.LabelServiceProxy;
import com.hybris.backoffice.search.utils.CategoryCatalogVersionMapper;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryFacetValueDisplayNameProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(CategoryFacetValueDisplayNameProvider.class);
    private CategoryCatalogVersionMapper categoryCatalogVersionMapper;
    private CatalogVersionService catalogVersionService;
    private LabelServiceProxy labelServiceProxy;
    private CategoryService categoryService;


    public String getDisplayName(String name, Locale locale)
    {
        try
        {
            return this.labelServiceProxy.getObjectLabel(getEncodedCategory(name), locale);
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
                LOG.debug("Can't decode category: {}", code);
            }
            return getCategory(code);
        }
        List<CatalogVersionModel> catalogVersions = Collections.singletonList(this.catalogVersionService.getCatalogVersion(decoded.catalogId, decoded.catalogVersion));
        return getCategoryForCatalogVersions(catalogVersions, decoded.categoryCode);
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
                LOG.error("Exception for categoryService.getCategoryForCode", (Throwable)uie);
            }
        }
        return null;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public CategoryCatalogVersionMapper getCategoryCatalogVersionMapper()
    {
        return this.categoryCatalogVersionMapper;
    }


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


    public void setCategoryService(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }
}
