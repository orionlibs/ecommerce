package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.servicelayer.search.SearchResult;

public interface CMSMediaDao extends Dao
{
    SearchResult<MediaModel> findMediasForCatalogVersion(String paramString1, String paramString2, CatalogVersionModel paramCatalogVersionModel, PageableData paramPageableData);
}
