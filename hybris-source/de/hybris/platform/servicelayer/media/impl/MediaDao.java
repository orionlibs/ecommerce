package de.hybris.platform.servicelayer.media.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.DerivedMediaModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.List;

public interface MediaDao extends Dao
{
    @Deprecated(since = "ages", forRemoval = true)
    List<MediaModel> findMedia(CatalogVersionModel paramCatalogVersionModel, String paramString);


    List<MediaModel> findMediaByCode(CatalogVersionModel paramCatalogVersionModel, String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    List<MediaModel> findMedia(String paramString);


    List<DerivedMediaModel> findMediaVersion(MediaModel paramMediaModel, String paramString);


    List<MediaModel> findMediaByCode(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    List<MediaFolderModel> findFolder(String paramString);


    List<MediaFolderModel> findMediaFolderByQualifier(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    List<MediaFormatModel> findFormat(String paramString);


    List<MediaFormatModel> findMediaFormatByQualifier(String paramString);


    MediaModel findMediaByFormat(MediaContainerModel paramMediaContainerModel, MediaFormatModel paramMediaFormatModel);


    List<MediaModel> findForeignDataOwnerdByMedia(MediaModel paramMediaModel);
}
