package de.hybris.platform.site;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import java.util.Collection;
import java.util.List;

public interface BaseSiteService
{
    Collection<BaseSiteModel> getAllBaseSites();


    BaseSiteModel getBaseSiteForUID(String paramString);


    BaseSiteModel getCurrentBaseSite();


    void setCurrentBaseSite(BaseSiteModel paramBaseSiteModel, boolean paramBoolean);


    void setCurrentBaseSite(String paramString, boolean paramBoolean);


    List<CatalogModel> getProductCatalogs(BaseSiteModel paramBaseSiteModel);
}
