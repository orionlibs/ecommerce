package de.hybris.platform.jalo.media;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.media.services.MimeService;
import de.hybris.platform.media.storage.LocalMediaFileCacheService;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageRegistry;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.media.storage.impl.StoredMediaData;
import de.hybris.platform.tx.Transaction;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class MediaDataStoreCommand implements MediaDataCommand<StoredMediaData>
{
    private static final Logger LOG = Logger.getLogger(MediaDataStoreCommand.class);
    private final PK mediaPK;
    private final Long oldMediaDataPk;
    private final String oldFolderQualifier;
    private final String oldLocation;
    private final String folderQualifier;
    private final String folderPath;
    private final String fileName;
    private final MediaStorageConfigService.MediaFolderConfig folderConfig;
    private final MediaStorageStrategy storageStrategy;
    private final LocalMediaFileCacheService cacheService;
    private final MimeService mimeService;
    private final boolean cacheConfigured;
    private InputStream dataStream;
    private String detectedMime;


    private MediaDataStoreCommand(Builder builder)
    {
        ApplicationContext appCtx = Registry.getApplicationContext();
        MediaStorageConfigService cfgService = (MediaStorageConfigService)appCtx.getBean("mediaStorageConfigService", MediaStorageConfigService.class);
        MediaStorageRegistry mediaStorageRegistry = (MediaStorageRegistry)appCtx.getBean("mediaStorageRegistry", MediaStorageRegistry.class);
        this.cacheService = (LocalMediaFileCacheService)appCtx.getBean("localMediaFileCacheService", LocalMediaFileCacheService.class);
        this.mimeService = (MimeService)appCtx.getBean("mimeService", MimeService.class);
        this.mediaPK = builder.mediaPK;
        this.oldMediaDataPk = builder.oldMediaDataPk;
        this.oldFolderQualifier = builder.oldFolderQualifier;
        this.oldLocation = builder.oldLocation;
        this.folderQualifier = builder.folderQualifier;
        this.folderPath = builder.folderPath;
        this.fileName = builder.fileName;
        this.folderConfig = cfgService.getConfigForFolder(this.folderQualifier);
        this.storageStrategy = mediaStorageRegistry.getStorageStrategyForFolder(this.folderConfig);
        this.cacheConfigured = isStorageCacheConfigured(this.folderConfig);
        detectAndSetMimeAndDataStream(builder.dataStream, builder.overrideMime);
    }


    private boolean isStorageCacheConfigured(MediaStorageConfigService.MediaFolderConfig config)
    {
        return (config.isLocalCacheEnabled() && !(this.storageStrategy instanceof de.hybris.platform.media.storage.LocalStoringStrategy));
    }


    private void detectAndSetMimeAndDataStream(InputStream dataStream, String overrideMime)
    {
        try
        {
            int availableBytes = dataStream.available();
            int bytesToRead = (availableBytes < 20) ? availableBytes : 20;
            if(bytesToRead > 0)
            {
                PushbackInputStream pis = new PushbackInputStream(dataStream, bytesToRead);
                byte[] firstBytes = new byte[bytesToRead];
                pis.read(firstBytes, 0, bytesToRead);
                pis.unread(firstBytes);
                this.dataStream = pis;
                this.detectedMime = this.mimeService.getBestMime(this.fileName, firstBytes, overrideMime);
            }
            else
            {
                this.dataStream = dataStream;
                this.detectedMime = StringUtils.isNotEmpty(overrideMime) ? overrideMime : "application/octet-stream";
            }
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public StoredMediaData execute()
    {
        Long dataPk = createNewDataPk();
        StoredMediaData storedMedia = null;
        Transaction.current().executeOnCommit(getOnCommitExecution());
        if(this.cacheConfigured)
        {
            InputStream tempFileStream = null;
            File tempFile = null;
            try
            {
                tempFile = copyToTempFile(this.folderQualifier, dataPk.toString(), this.dataStream);
                tempFileStream = new FileInputStream(tempFile);
                Map<String, Object> metaData = buildMediaMetaData(this.detectedMime, this.fileName, this.folderPath,
                                Long.valueOf(tempFile.length()));
                storedMedia = this.storageStrategy.store(this.folderConfig, dataPk.toString(), metaData, tempFileStream);
                preLoadCache(storedMedia.getLocation(), tempFile);
            }
            catch(FileNotFoundException e)
            {
                throw new JaloSystemException(e);
            }
            finally
            {
                IOUtils.closeQuietly(tempFileStream);
                FileUtils.deleteQuietly(tempFile);
            }
        }
        else
        {
            Map<String, Object> metaData = buildMediaMetaData(this.detectedMime, this.fileName, this.folderPath, null);
            storedMedia = this.storageStrategy.store(this.folderConfig, dataPk.toString(), metaData, this.dataStream);
        }
        storedMedia.setDataPk(dataPk);
        Transaction.current().executeOnRollback(getOnRollbackExecution(storedMedia));
        return storedMedia;
    }


    private Long createNewDataPk()
    {
        return Long.valueOf(PK.createCounterPK(30).getLongValue());
    }


    private <MediaID> File copyToTempFile(String folderQualifier, MediaID mediaId, InputStream dataStream)
    {
        OutputStream outputStream = null;
        File tempFile = null;
        try
        {
            outputStream = new FileOutputStream(tempFile = File.createTempFile(folderQualifier + "_" + folderQualifier, "upload"));
            IOUtils.copy(dataStream, outputStream);
        }
        catch(IOException e)
        {
            FileUtils.deleteQuietly(tempFile);
            throw new JaloSystemException(e);
        }
        finally
        {
            IOUtils.closeQuietly(dataStream);
            IOUtils.closeQuietly(outputStream);
        }
        return tempFile;
    }


    private Map<String, Object> buildMediaMetaData(String mime, String originalName, String folderPath, Long size)
    {
        Map<String, Object> metaData = new HashMap<>();
        metaData.put("mime", mime);
        metaData.put("fileName", originalName);
        metaData.put("folderPath", folderPath);
        metaData.put("size", size);
        return metaData;
    }


    private void preLoadCache(String location, File tempFile)
    {
        InputStream cachedFileStream = null;
        try
        {
            cachedFileStream = this.cacheService.storeOrGetAsStream(this.folderConfig, location, (LocalMediaFileCacheService.StreamGetter)new Object(this, tempFile));
        }
        finally
        {
            IOUtils.closeQuietly(cachedFileStream);
        }
    }


    private Transaction.TransactionAwareExecution getOnCommitExecution()
    {
        return (Transaction.TransactionAwareExecution)new Object(this);
    }


    private Transaction.TransactionAwareExecution getOnRollbackExecution(StoredMediaData storedMediaData)
    {
        return (Transaction.TransactionAwareExecution)new Object(this, storedMediaData);
    }


    public static Builder builder()
    {
        return new Builder();
    }
}
