package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.servicelayer.search.SearchResult;

public interface CMSItemDao
{
    CMSItemModel findByUid(String paramString, CatalogVersionModel paramCatalogVersionModel);


    SearchResult<CMSItemModel> findByTypeCodeAndName(CatalogVersionModel paramCatalogVersionModel, String paramString1, String paramString2);
}
