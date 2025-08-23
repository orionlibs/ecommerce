package de.hybris.platform.commercefacades.catalog.data;

import java.io.Serializable;
import java.util.List;

public class CatalogsData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CatalogData> catalogs;


    public void setCatalogs(List<CatalogData> catalogs)
    {
        this.catalogs = catalogs;
    }


    public List<CatalogData> getCatalogs()
    {
        return this.catalogs;
    }
}
