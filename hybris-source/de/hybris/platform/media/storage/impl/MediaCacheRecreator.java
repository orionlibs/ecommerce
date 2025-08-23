package de.hybris.platform.media.storage.impl;

import com.google.common.base.Splitter;
import de.hybris.platform.core.Registry;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.util.MediaUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class MediaCacheRecreator
{
    private static final Logger LOG = Logger.getLogger(MediaCacheRecreator.class);
    private static final Splitter CACHED_FILENAME_SPLITTER = Splitter.on("__H__");
    private final CacheController cacheController;
    private final File mainDataDir;
    String tenantId;


    public MediaCacheRecreator(File mainDataDir, CacheController cacheController)
    {
        this.mainDataDir = mainDataDir;
        this.cacheController = cacheController;
    }


    public void init()
    {
        this.tenantId = Registry.getCurrentTenantNoFallback().getTenantID();
    }


    public void recreateCache(String defaultCacheFolderName, Iterable<MediaStorageConfigService.MediaFolderConfig> folderConfigs)
    {
        Set<String> processedCaches = new HashSet<>();
        processedCaches.add(defaultCacheFolderName);
        recreateCacheFromDiskFolder(this.mainDataDir, defaultCacheFolderName);
        for(MediaStorageConfigService.MediaFolderConfig folderConfig : folderConfigs)
        {
            if(folderConfig.isLocalCacheEnabled())
            {
                String cacheFolder = folderConfig.getParameter(DefaultMediaStorageConfigService.DefaultSettingKeys.LOCAL_CACHE_ROOT_FOLDER_KEY.getKey());
                if(StringUtils.isNotBlank(cacheFolder) && !processedCaches.contains(cacheFolder))
                {
                    processedCaches.add(cacheFolder);
                    recreateCacheFromDiskFolder(this.mainDataDir, cacheFolder);
                }
            }
        }
    }


    private void recreateCacheFromDiskFolder(File mainDataDir, String cacheFolderName)
    {
        File cacheFolder = MediaUtil.composeOrGetParent(mainDataDir, cacheFolderName);
        if(cacheFolder.exists() && cacheFolder.isDirectory())
        {
            try
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(">>> Recreating media cache region using existing cached files [cache folder: " + cacheFolder + "]");
                }
                MediaCacheFileVisitor mediaCacheFileVisitor = new MediaCacheFileVisitor(this, cacheFolderName);
                Files.walkFileTree(cacheFolder.toPath(), (FileVisitor<? super Path>)mediaCacheFileVisitor);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(">>> Media cache region recreated successfully [number of processed files: " + mediaCacheFileVisitor
                                    .getNumFiles() + "]");
                }
                deleteEvictedFiles(mediaCacheFileVisitor);
            }
            catch(IOException e)
            {
                LOG.error("Cannot read cache folder: " + cacheFolder + " [reason: " + e.getMessage() + "]");
            }
        }
    }


    private void deleteEvictedFiles(MediaCacheFileVisitor mediaCacheFileVisitor)
    {
        for(File evictedFile : mediaCacheFileVisitor.getEvictedFiles())
        {
            if(evictedFile.exists())
            {
                boolean deleted = FileUtils.deleteQuietly(evictedFile);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("File marked for remove on eviction: " + evictedFile.getAbsolutePath() + ". Removed? " + deleted);
                }
            }
        }
    }
}
