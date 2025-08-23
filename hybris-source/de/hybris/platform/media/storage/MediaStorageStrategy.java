package de.hybris.platform.media.storage;

import de.hybris.platform.media.storage.impl.StoredMediaData;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

public interface MediaStorageStrategy
{
    StoredMediaData store(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig, String paramString, Map<String, Object> paramMap, InputStream paramInputStream);


    void delete(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig, String paramString);


    InputStream getAsStream(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig, String paramString);


    File getAsFile(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig, String paramString);


    long getSize(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig, String paramString);


    default boolean hasValidMediaFolderName(MediaStorageConfigService.MediaFolderConfig config)
    {
        return true;
    }
}
