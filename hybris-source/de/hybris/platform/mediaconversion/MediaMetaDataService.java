package de.hybris.platform.mediaconversion;

import de.hybris.platform.core.model.media.MediaModel;
import java.util.Map;

public interface MediaMetaDataService
{
    void extractAllMetaData(MediaModel paramMediaModel);


    void deleteAllMetaData(MediaModel paramMediaModel);


    Map<String, String> getMetaData(MediaModel paramMediaModel, String paramString);


    Map<String, Map<String, String>> getAllMetaData(MediaModel paramMediaModel);
}
