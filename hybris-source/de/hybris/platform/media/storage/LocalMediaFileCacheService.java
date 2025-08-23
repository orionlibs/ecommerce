package de.hybris.platform.media.storage;

import java.io.File;
import java.io.InputStream;

public interface LocalMediaFileCacheService
{
    InputStream storeOrGetAsStream(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig, String paramString, StreamGetter paramStreamGetter);


    File storeOrGetAsFile(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig, String paramString, StreamGetter paramStreamGetter);


    void removeFromCache(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig, String paramString);
}
