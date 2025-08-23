package de.hybris.platform.media.storage;

import java.util.Collection;

public interface MediaStorageConfigService
{
    MediaFolderConfig getConfigForFolder(String paramString);


    Iterable<MediaFolderConfig> getFolderConfigsForStrategy(String paramString);


    GlobalMediaStorageConfig getGlobalSettingsForStrategy(String paramString);


    String getDefaultStrategyId();


    boolean isStorageStrategyConfigured(String paramString);


    Collection<String> getSecuredFolders();


    String getDefaultCacheFolderName();
}
