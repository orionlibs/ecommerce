package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.servicelayer.search.SearchResult;

public interface CMSMediaFolderDao extends Dao
{
    SearchResult<MediaFolderModel> findMediaFolders(String paramString, PageableData paramPageableData);
}
