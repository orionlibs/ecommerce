package de.hybris.platform.media.url;

import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.storage.MediaStorageConfigService;

public interface MediaURLStrategy
{
    String getUrlForMedia(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig, MediaSource paramMediaSource);
}
