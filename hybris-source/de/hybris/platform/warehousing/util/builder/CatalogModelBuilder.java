package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.catalog.model.CatalogModel;
import java.util.Locale;

public class CatalogModelBuilder
{
    private final CatalogModel model = new CatalogModel();


    private CatalogModel getModel()
    {
        return this.model;
    }


    public static CatalogModelBuilder aModel()
    {
        return new CatalogModelBuilder();
    }


    public CatalogModel build()
    {
        return getModel();
    }


    public CatalogModelBuilder withId(String id)
    {
        getModel().setId(id);
        return this;
    }


    public CatalogModelBuilder withName(String name, Locale locale)
    {
        getModel().setName(name, locale);
        return this;
    }


    public CatalogModelBuilder withDefaultCatalog(Boolean defaultCatalog)
    {
        getModel().setDefaultCatalog(defaultCatalog);
        return this;
    }
}
