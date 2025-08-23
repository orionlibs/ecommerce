package de.hybris.platform.mediaconversion.metadata;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.MediaMetaDataModel;
import java.util.Collection;

public interface MediaMetaDataServiceDao
{
    Collection<MediaMetaDataModel> findMetaData(MediaModel paramMediaModel, String paramString);
}
