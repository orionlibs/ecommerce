package de.hybris.platform.cockpit.services.media;

import de.hybris.platform.core.model.media.MediaModel;
import java.util.Set;

public interface MediaInfoService
{
    Boolean isWebMedia(MediaModel paramMediaModel);


    Boolean isWebMedia(String paramString);


    Set<MediaInfo> getWebMediaInfos();


    Set<MediaInfo> getNonWebMediaInfos();


    String getFallbackIcon();


    MediaInfo getNonWebMediaInfo(String paramString);
}
