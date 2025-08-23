package de.hybris.platform.cms2.multicountry.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import java.util.Collection;
import java.util.List;

public interface CatalogLevelService
{
    List<CatalogVersionModel> getLevelCatalogVersions(AbstractPageModel paramAbstractPageModel, List<ContentCatalogModel> paramList);


    List<ContentSlotModel> getSortedMultiCountryContentSlots(List<ContentSlotModel> paramList);


    int getCatalogLevel(ContentCatalogModel paramContentCatalogModel);


    boolean isIntermediateLevel(ContentCatalogModel paramContentCatalogModel);


    boolean isBottomLevel(ContentCatalogModel paramContentCatalogModel);


    boolean isTopLevel(ContentCatalogModel paramContentCatalogModel);


    Collection<ContentCatalogModel> getAllSubCatalogs(ContentCatalogModel paramContentCatalogModel);


    @Deprecated(since = "2105")
    Collection<ContentCatalogModel> getAllSuperCatalogs(ContentCatalogModel paramContentCatalogModel, CMSSiteModel paramCMSSiteModel);


    Collection<ContentCatalogModel> getAllSuperCatalogs(ContentCatalogModel paramContentCatalogModel);
}
