package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.servicelayer.search.SearchResult;

public interface CMSMediaContainerDao extends Dao
{
    MediaContainerModel getMediaContainerForQualifier(String paramString, CatalogVersionModel paramCatalogVersionModel);


    SearchResult<MediaContainerModel> findMediaContainersForCatalogVersion(String paramString, CatalogVersionModel paramCatalogVersionModel, PageableData paramPageableData);
}
