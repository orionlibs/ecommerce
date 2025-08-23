package de.hybris.platform.media.storage.impl;

import com.google.common.collect.Iterables;
import de.hybris.platform.core.Registry;
import de.hybris.platform.media.storage.LocalMediaFileCacheService;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageRegistry;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.key.CacheKey;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultLocalMediaFileCacheService implements LocalMediaFileCacheService
{
    private static final Logger LOG = Logger.getLogger(DefaultLocalMediaFileCacheService.class);
    private static final String DEFAULT_CACHE_FOLDER = "cache";
    private static final int GET_RESOURCE_MAX_RETRIES = 5;
    public static final String CACHE_FILE_NAME_DELIM = "__H__";
    public static final String CACHE_FILE_NO_RESTORABLE = "_NR_";
    public static final String PREVENT_LONGFILENAMES_LOCALCACHE = "prevent.longfilenames.localcache";
    private MediaCacheRecreator cacheRecreator;
    private MediaStorageRegistry storageRegistry;
    private LocalFileMediaStorageStrategy storageStrategy;
    private MediaStorageConfigService storageConfigService;
    private CacheController cacheController;
    private MediaCacheRegion mediaCacheRegion;
    private File mainDataDir;
    private String tenantId;


    @PostConstruct
    public void init()
    {
        this.tenantId = Registry.getCurrentTenantNoFallback().getTenantID();
        this.cacheController.addLifecycleCallback((CacheLifecycleCallback)new MediaCacheLifecycleCallback());
        this.cacheRecreator.recreateCache(this.storageConfigService.getDefaultCacheFolderName(), getRemoteStorageFolderConfigs());
    }


    private Iterable<MediaStorageConfigService.MediaFolderConfig> getRemoteStorageFolderConfigs()
    {
        Set<MediaStorageConfigService.MediaFolderConfig> result = new HashSet<>();
        Map<String, MediaStorageStrategy> allStrategies = this.storageRegistry.getStorageStrategies();
        for(Map.Entry<String, MediaStorageStrategy> entry : allStrategies.entrySet())
        {
            String strategyId = entry.getKey();
            MediaStorageStrategy strategy = entry.getValue();
            if(!(strategy instanceof de.hybris.platform.media.storage.LocalStoringStrategy))
            {
                Iterables.addAll(result, this.storageConfigService.getFolderConfigsForStrategy(strategyId));
            }
        }
        return result;
    }


    public File storeOrGetAsFile(MediaStorageConfigService.MediaFolderConfig config, String location, LocalMediaFileCacheService.StreamGetter streamGetter)
    {
        File file = getMediaCacheFile(config, location, streamGetter);
        if(file == null)
        {
            throw new IllegalStateException("Cannot get cached file");
        }
        return file;
    }


    private File getMediaCacheFile(MediaStorageConfigService.MediaFolderConfig config, String location, LocalMediaFileCacheService.StreamGetter streamGetter)
    {
        File file;
        if(isStreamBiggerThanCacheSize(config, location, streamGetter))
        {
            file = getStreamAsTempFile(config, location, streamGetter);
        }
        else
        {
            file = (File)(new Object(this)).loadResource(config, location, streamGetter);
        }
        return file;
    }


    private static File getStreamAsTempFile(MediaStorageConfigService.MediaFolderConfig config, String location, LocalMediaFileCacheService.StreamGetter streamGetter)
    {
        File file = null;
        InputStream stream = streamGetter.getStream(config, location);
        try
        {
            file = File.createTempFile(location, "tmp");
            OutputStream out = Files.newOutputStream(file.toPath(), new OpenOption[] {StandardOpenOption.DELETE_ON_CLOSE});
            try
            {
                IOUtils.copy(stream, out);
                if(out != null)
                {
                    out.close();
                }
            }
            catch(Throwable throwable)
            {
                if(out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            FileUtils.deleteQuietly(file);
            throw new IllegalStateException("Cannot create temporary file for requested media from the storage [reason: " + e
                            .getMessage() + "]", e);
        }
        return file;
    }


    public InputStream storeOrGetAsStream(MediaStorageConfigService.MediaFolderConfig config, String location, LocalMediaFileCacheService.StreamGetter streamGetter)
    {
        InputStream stream = getMediaCacheStream(config, location, streamGetter);
        if(stream == null)
        {
            throw new IllegalStateException("Cannot get cached file stream");
        }
        return stream;
    }


    private InputStream getMediaCacheStream(MediaStorageConfigService.MediaFolderConfig config, String location, LocalMediaFileCacheService.StreamGetter streamGetter)
    {
        InputStream stream;
        if(isStreamBiggerThanCacheSize(config, location, streamGetter))
        {
            stream = streamGetter.getStream(config, location);
        }
        else
        {
            stream = (InputStream)(new Object(this)).loadResource(config, location, streamGetter);
        }
        return stream;
    }


    private boolean isStreamBiggerThanCacheSize(MediaStorageConfigService.MediaFolderConfig config, String location, LocalMediaFileCacheService.StreamGetter streamGetter)
    {
        long sizeInBytes = streamGetter.getSize(config, location);
        int cacheUnitWeight = MediaCacheUnit.convertNumBytesToCacheUnitWeight(sizeInBytes);
        return (cacheUnitWeight > this.mediaCacheRegion.getCacheMaxEntries());
    }


    public void removeFromCache(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        this.cacheController.invalidate((CacheKey)new MediaCacheKey(
                        Registry.getCurrentTenant().getTenantID(), getCacheFolder(config), location));
    }


    private static String getCacheFolderPath(MediaStorageConfigService.MediaFolderConfig config)
    {
        String rootCacheFolder = getCacheFolder(config);
        return rootCacheFolder + "/" + rootCacheFolder;
    }


    private static String getCacheFolder(MediaStorageConfigService.MediaFolderConfig config)
    {
        return (String)config.getParameter(DefaultMediaStorageConfigService.DefaultSettingKeys.LOCAL_CACHE_ROOT_FOLDER_KEY.getKey(), String.class, "cache");
    }


    @Required
    public void setMainDataDir(File mainDataDir)
    {
        this.mainDataDir = mainDataDir;
    }


    @Required
    public void setCacheController(CacheController cacheController)
    {
        this.cacheController = cacheController;
    }


    @Required
    public void setStorageRegistry(MediaStorageRegistry storageRegistry)
    {
        this.storageRegistry = storageRegistry;
    }


    @Required
    public void setStorageStrategy(LocalFileMediaStorageStrategy storageStrategy)
    {
        this.storageStrategy = storageStrategy;
    }


    @Required
    public void setMediaCacheRegion(MediaCacheRegion mediaCacheRegion)
    {
        this.mediaCacheRegion = mediaCacheRegion;
    }


    @Required
    public void setStorageConfigService(MediaStorageConfigService storageConfigService)
    {
        this.storageConfigService = storageConfigService;
    }


    @Required
    public void setCacheRecreator(MediaCacheRecreator cacheRecreator)
    {
        this.cacheRecreator = cacheRecreator;
    }
}
