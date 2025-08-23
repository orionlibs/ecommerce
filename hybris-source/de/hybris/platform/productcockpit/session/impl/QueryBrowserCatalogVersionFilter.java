package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.session.BrowserFilter;
import java.util.Collections;

public class QueryBrowserCatalogVersionFilter implements BrowserFilter
{
    private final CatalogVersionModel catalogVersion;


    public QueryBrowserCatalogVersionFilter(CatalogVersionModel catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public boolean exclude(Object item)
    {
        return !(item instanceof de.hybris.platform.jalo.product.Product);
    }


    public void filterQuery(Query query)
    {
        query.setContextParameter("catalogVersionFilter",
                        Collections.singletonList(this.catalogVersion));
    }


    public String getLabel()
    {
        return "### FILTERED (only items from catalog version " + this.catalogVersion.getMnemonic() + ") ###";
    }
}
