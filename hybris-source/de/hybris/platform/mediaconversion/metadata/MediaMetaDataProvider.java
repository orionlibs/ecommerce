package de.hybris.platform.mediaconversion.metadata;

import de.hybris.platform.core.model.media.MediaModel;
import java.util.Set;

public interface MediaMetaDataProvider
{
    void extractMetaData(MediaModel paramMediaModel);


    Set<String> getGroupNames();
}
