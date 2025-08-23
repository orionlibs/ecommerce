package de.hybris.platform.cms2.servicelayer.data.impl;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DefaultCMSDataFactory implements CMSDataFactory
{
    private static final Logger LOG = Logger.getLogger(DefaultCMSDataFactory.class.getName());
    protected CMSSiteService cmsSiteService;
    protected ProductService productService;
    protected CategoryService categoryService;
    protected CatalogService catalogService;


    public ContentSlotData createContentSlotData(ContentSlotForPageModel csForPage)
    {
        return (ContentSlotData)new DefaultContentSlotData(csForPage);
    }


    public ContentSlotData createContentSlotData(AbstractPageModel page, ContentSlotForTemplateModel csForTemplate)
    {
        return (ContentSlotData)new DefaultContentSlotData(page, csForTemplate);
    }


    public ContentSlotData createContentSlotData(String pageId, ContentSlotModel contentSlot, String position, boolean fromMaster, boolean allowOverwrite)
    {
        return (ContentSlotData)new DefaultContentSlotData(pageId, contentSlot, position, fromMaster, allowOverwrite);
    }


    public RestrictionData createRestrictionData()
    {
        return (RestrictionData)new DefaultRestrictionData();
    }


    public RestrictionData createRestrictionData(String categoryCode, String productCode, String catalogCode)
    {
        DefaultRestrictionData data = new DefaultRestrictionData();
        if(!StringUtils.isEmpty(categoryCode))
        {
            try
            {
                data.setCategory(this.categoryService.getCategoryForCode(categoryCode));
            }
            catch(Exception e)
            {
                LOG.info("Could not find category with code <" + categoryCode + ">", e);
            }
        }
        if(!StringUtils.isEmpty(productCode))
        {
            try
            {
                data.setProduct(this.productService.getProductForCode(productCode));
            }
            catch(Exception e)
            {
                LOG.info("Could not find product with code <" + productCode + ">", e);
            }
        }
        if(!StringUtils.isEmpty(catalogCode))
        {
            try
            {
                data.setCatalog(this.catalogService.getCatalogForId(catalogCode));
            }
            catch(Exception e)
            {
                LOG.info("Could not find catalog with code <" + catalogCode + ">", e);
            }
        }
        return (RestrictionData)data;
    }


    public RestrictionData createRestrictionData(CategoryModel category, ProductModel product)
    {
        DefaultRestrictionData data = new DefaultRestrictionData();
        if(category != null)
        {
            data.setCategory(category);
        }
        if(product != null)
        {
            data.setProduct(product);
        }
        CatalogVersionModel version = this.cmsSiteService.getCurrentCatalogVersion();
        if(version != null)
        {
            data.setCatalog(version.getCatalog());
        }
        return (RestrictionData)data;
    }


    public RestrictionData createRestrictionData(ProductModel product)
    {
        DefaultRestrictionData defaultRestrictionData = new DefaultRestrictionData();
        defaultRestrictionData.setProduct(product);
        return (RestrictionData)defaultRestrictionData;
    }


    public RestrictionData createRestrictionData(CategoryModel category)
    {
        DefaultRestrictionData defaultRestrictionData = new DefaultRestrictionData();
        defaultRestrictionData.setCategory(category);
        return (RestrictionData)defaultRestrictionData;
    }


    public RestrictionData createRestrictionData(CatalogModel catalog)
    {
        DefaultRestrictionData defaultRestrictionData = new DefaultRestrictionData();
        defaultRestrictionData.setCatalog(catalog);
        return (RestrictionData)defaultRestrictionData;
    }


    public RestrictionData createRestrictionData(String key, Object value)
    {
        DefaultRestrictionData defaultRestrictionData = new DefaultRestrictionData();
        defaultRestrictionData.setValue(key, value);
        return (RestrictionData)defaultRestrictionData;
    }


    public void setCmsSiteService(CMSSiteService cmsSiteService)
    {
        this.cmsSiteService = cmsSiteService;
    }


    public void setCategoryService(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }


    public void setCatalogService(CatalogService catalogService)
    {
        this.catalogService = catalogService;
    }


    public void setProductService(ProductService productService)
    {
        this.productService = productService;
    }
}
