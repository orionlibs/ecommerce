package de.hybris.platform.media.storage;

import de.hybris.platform.media.url.MediaURLStrategy;
import java.util.Collection;
import java.util.Map;

public interface MediaStorageRegistry
{
    MediaStorageStrategy getStorageStrategyForFolder(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig);


    MediaURLStrategy getURLStrategyForFolder(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig, Collection<String> paramCollection);


    Map<String, MediaStorageStrategy> getStorageStrategies();
}
