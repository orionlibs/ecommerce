package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.productcockpit.services.catalog.CatalogService;
import org.zkoss.zkplus.spring.SpringUtil;

public class CollectionBrowserCatalogVersionFilter implements BrowserFilter
{
    private final CatalogVersionModel catalogVersion;
    private CatalogService productCockpitCatalogService;


    public CollectionBrowserCatalogVersionFilter(CatalogVersionModel catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public boolean exclude(Object item)
    {
        if(this.catalogVersion != null && item instanceof TypedObject)
        {
            Object object = ((TypedObject)item).getObject();
            if(object instanceof ProductModel)
            {
                ProductModel product = (ProductModel)object;
                CatalogVersionModel cv = getProductCockpitCatalogService().getCatalogVersion(product.getPk());
                return this.catalogVersion.equals(cv);
            }
        }
        return false;
    }


    public void filterQuery(Query query)
    {
    }


    public String getLabel()
    {
        return "### FILTERED (only items from catalog version " + this.catalogVersion.getMnemonic() + ") ###";
    }


    public CatalogService getProductCockpitCatalogService()
    {
        if(this.productCockpitCatalogService == null)
        {
            this.productCockpitCatalogService = (CatalogService)SpringUtil.getBean("productCockpitCatalogService");
        }
        return this.productCockpitCatalogService;
    }
}
