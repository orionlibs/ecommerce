package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.catalog.daos.CatalogVersionDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.warehousing.util.builder.CatalogVersionModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class CatalogVersions extends AbstractItems<CatalogVersionModel>
{
    public static final String VERSION_STAGING = "staging";
    public static final String VERSION_ONLINE = "Online";
    private CatalogVersionDao catalogVersionDao;
    private Catalogs catalogs;
    private Currencies currencies;
    private ContentCatalogs contentCatalogs;


    public CatalogVersionModel Staging()
    {
        return Staging(getCatalogs().Primary());
    }


    public CatalogVersionModel Staging(CatalogModel catalogModel)
    {
        return (CatalogVersionModel)getFromCollectionOrSaveAndReturn(() -> getCatalogVersionDao().findCatalogVersions("primary", "staging"),
                        () -> CatalogVersionModelBuilder.aModel().withCatalog(catalogModel).withDefaultCurrency(getCurrencies().AmericanDollar()).withActive(Boolean.TRUE).withVersion("staging").build());
    }


    public CatalogVersionModel Online()
    {
        return (CatalogVersionModel)getFromCollectionOrSaveAndReturn(() -> getCatalogVersionDao().findCatalogVersions("contentCatalog_online", "Online"),
                        () -> CatalogVersionModelBuilder.aModel().withDefaultCurrency(getCurrencies().AmericanDollar()).withActive(Boolean.TRUE).withVersion("Online").withCatalog((CatalogModel)getContentCatalogs().contentCatalog_online()).build());
    }


    public CatalogVersionDao getCatalogVersionDao()
    {
        return this.catalogVersionDao;
    }


    @Required
    public void setCatalogVersionDao(CatalogVersionDao catalogVersionDao)
    {
        this.catalogVersionDao = catalogVersionDao;
    }


    public Catalogs getCatalogs()
    {
        return this.catalogs;
    }


    @Required
    public void setCatalogs(Catalogs catalogs)
    {
        this.catalogs = catalogs;
    }


    public ContentCatalogs getContentCatalogs()
    {
        return this.contentCatalogs;
    }


    @Required
    public void setContentCatalogs(ContentCatalogs contentCatalogs)
    {
        this.contentCatalogs = contentCatalogs;
    }


    public Currencies getCurrencies()
    {
        return this.currencies;
    }


    @Required
    public void setCurrencies(Currencies currencies)
    {
        this.currencies = currencies;
    }
}
