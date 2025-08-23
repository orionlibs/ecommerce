package de.hybris.platform.commercefacades.catalog.data;

import java.util.Collection;

public class CatalogData extends AbstractCatalogItemData
{
    private Collection<CatalogVersionData> catalogVersions;


    public void setCatalogVersions(Collection<CatalogVersionData> catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }


    public Collection<CatalogVersionData> getCatalogVersions()
    {
        return this.catalogVersions;
    }
}
