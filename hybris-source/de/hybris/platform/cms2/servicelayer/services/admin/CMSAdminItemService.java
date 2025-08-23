package de.hybris.platform.cms2.servicelayer.services.admin;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.CMSItemData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Optional;

public interface CMSAdminItemService
{
    CMSItemModel findByUid(String paramString, CatalogVersionModel paramCatalogVersionModel) throws CMSItemNotFoundException;


    CMSItemModel findByUid(String paramString) throws CMSItemNotFoundException;


    Optional<CMSItemModel> findByItemData(CMSItemData paramCMSItemData);


    <T extends CMSItemModel> T createItem(Class<T> paramClass);


    SearchResult<CMSItemModel> findByTypeCodeAndName(CatalogVersionModel paramCatalogVersionModel, String paramString1, String paramString2);
}
