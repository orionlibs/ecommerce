package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import java.net.URL;
import java.util.Collection;
import java.util.List;

public interface CMSSiteService
{
    boolean containsCatalog(CMSSiteModel paramCMSSiteModel, CatalogModel paramCatalogModel, boolean paramBoolean);


    Collection<CatalogModel> getAllCatalogs(CMSSiteModel paramCMSSiteModel);


    List<CatalogModel> getClassificationCatalogs(CMSSiteModel paramCMSSiteModel);


    CatalogVersionModel getCurrentCatalogVersion();


    CMSSiteModel getCurrentSite();


    CMSSiteModel getSiteForURL(URL paramURL) throws CMSItemNotFoundException;


    Collection<CMSSiteModel> getSites();


    String getStartPageLabelOrId(CMSSiteModel paramCMSSiteModel);


    boolean hasCurrentCatalogVersion();


    boolean hasCurrentSite();


    void setCurrentCatalogVersion(CatalogVersionModel paramCatalogVersionModel) throws CMSItemNotFoundException;


    void setCurrentSite(CMSSiteModel paramCMSSiteModel);


    CMSSiteModel setCurrentSite(URL paramURL, PreviewDataModel paramPreviewDataModel) throws CMSItemNotFoundException;


    void setCurrentSiteAndCatalogVersions(CMSSiteModel paramCMSSiteModel, boolean paramBoolean) throws CMSItemNotFoundException;


    CMSSiteModel setCurrentSiteAndCatalogVersions(String paramString, boolean paramBoolean) throws CMSItemNotFoundException;


    CMSSiteModel setCurrentSiteAndCatalogVersionsForURL(URL paramURL, boolean paramBoolean) throws CMSItemNotFoundException;


    CatalogVersionModel getCurrentSiteDefaultContentCatalogActiveVersion();
}
