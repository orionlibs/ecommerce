package de.hybris.platform.azure.media.storage;

import com.google.common.base.Preconditions;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobProperties;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import de.hybris.platform.azure.media.AzureCloudUtils;
import de.hybris.platform.media.exceptions.ExternalStorageServiceException;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.exceptions.MediaRemovalException;
import de.hybris.platform.media.exceptions.MediaStoreException;
import de.hybris.platform.media.services.MediaLocationHashService;
import de.hybris.platform.media.services.MediaStorageInitializer;
import de.hybris.platform.media.services.impl.HierarchicalMediaPathBuilder;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.media.storage.impl.StoredMediaData;
import de.hybris.platform.util.MediaUtil;
import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class WindowsAzureBlobStorageStrategy implements MediaStorageStrategy, MediaStorageInitializer
{
    private static final Logger LOG = LoggerFactory.getLogger(WindowsAzureBlobStorageStrategy.class);
    public static final String CONNECTION_STRING_KEY = "connection";
    public static final String CONTAINER_ADDRESS_KEY = "containerAddress";
    private static final String AZURE_MEDIA_STORAGE_STRATEGY = "windowsAzureBlobStorageStrategy";
    private boolean cleanOnInit = true;
    private MediaLocationHashService locationHashService;
    private MediaStorageConfigService storageConfigService;


    public StoredMediaData store(MediaStorageConfigService.MediaFolderConfig config, String mediaId, Map<String, Object> metaData, InputStream dataStream)
    {
        Preconditions.checkArgument((config != null), "folder config is required!");
        Preconditions.checkArgument((mediaId != null), "mediaId is required!");
        Preconditions.checkArgument((metaData != null), "metaData is required!");
        Preconditions.checkArgument((dataStream != null), "dataStream is required!");
        Object size = metaData.get("size");
        String mime = (String)metaData.get("mime");
        Preconditions.checkArgument(size instanceof Long, "Object size as Long is required to store blobs in Azure Blob Storage. Only local caching allows to get size.");
        try
        {
            String location = assembleLocation(mediaId, (String)metaData.get("fileName"));
            CloudBlockBlob blob = getBlockBlobReference(config, location);
            blob.upload(dataStream, ((Long)size).longValue());
            BlobProperties props = blob.getProperties();
            props.setCacheControl("public, max-age=3600");
            if(metaData.get("mime") != null)
            {
                props.setContentType((String)metaData.get("mime"));
            }
            blob.uploadProperties();
            String blobName = blob.getName();
            String hashForLocation = this.locationHashService.createHash(config.getFolderQualifier(), blobName, ((Long)size).longValue(), mime);
            return new StoredMediaData(blobName, hashForLocation, ((Long)size).longValue(), mime);
        }
        catch(Exception e)
        {
            throw new MediaStoreException(e);
        }
    }


    protected String assembleLocation(String mediaId, String realFileName)
    {
        HierarchicalMediaPathBuilder pathBuilder = HierarchicalMediaPathBuilder.forDepth(2);
        String location = pathBuilder.buildPath(null, mediaId) + pathBuilder.buildPath(null, mediaId);
        if(StringUtils.isNotBlank(realFileName))
        {
            String fileName = realFileName;
            boolean hasControlCharacters = realFileName.chars().anyMatch(Character::isISOControl);
            if(hasControlCharacters)
            {
                LOG.warn("Control characters detected in realFileName of media with id: " + mediaId + ". Check file on disk.");
                String stripped = ((StringBuilder)realFileName.chars().filter(Character::isISOControl.negate()).<StringBuilder>collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)).toString();
                if(StringUtils.isNotBlank(stripped))
                {
                    fileName = stripped;
                }
                else
                {
                    LOG.warn("The realFileName of media with id: " + mediaId + " is empty after removing control characters. It will not be used.");
                }
            }
            location = MediaUtil.addTrailingFileSepIfNeeded(location) + MediaUtil.addTrailingFileSepIfNeeded(location);
        }
        return location.replaceAll("\\\\", "/");
    }


    public void delete(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        Preconditions.checkArgument((config != null), "config is required!");
        Preconditions.checkArgument((location != null), "location is required!");
        try
        {
            CloudBlockBlob blob = getBlockBlobReference(config, location);
            blob.delete();
        }
        catch(StorageException e)
        {
            if(!"blobNotFound".equalsIgnoreCase(e.getErrorCode()))
            {
                throw new MediaRemovalException("Removal of media: " + location + " has failed.", e);
            }
        }
        catch(Exception e)
        {
            throw new MediaRemovalException("Removal of media: " + location + " has failed.", e);
        }
    }


    public InputStream getAsStream(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        Preconditions.checkArgument((config != null), "folderQualifier is required!");
        Preconditions.checkArgument((location != null), "location is required!");
        try
        {
            CloudBlockBlob blob = getBlockBlobReference(config, location);
            return (InputStream)blob.openInputStream();
        }
        catch(Exception e)
        {
            throw new MediaNotFoundException("Media not found (requested media location: " + location + ")", e);
        }
    }


    private CloudBlockBlob getBlockBlobReference(MediaStorageConfigService.MediaFolderConfig config, String location) throws Exception
    {
        CloudBlobClient blobClient = getCloudBlobClient(config);
        CloudBlobContainer container = getContainerForFolder(config, blobClient);
        return container.getBlockBlobReference(location);
    }


    private CloudBlobContainer getContainerForFolder(MediaStorageConfigService.MediaFolderConfig config, CloudBlobClient blobClient) throws Exception
    {
        Integer numRetries = (Integer)config.getParameter("createContainer.numRetries", Integer.class);
        Integer delayInSeconds = (Integer)config.getParameter("createContainer.delayInSeconds", Integer.class);
        for(int retries = 0; ; retries++)
        {
            String containerName = AzureCloudUtils.computeContainerAddress(config);
            try
            {
                CloudBlobContainer container = blobClient.getContainerReference(containerName);
                container.createIfNotExists();
                return container;
            }
            catch(StorageException e)
            {
                if(retries < numRetries.intValue())
                {
                    Duration sleepTime = Duration.ofSeconds(delayInSeconds.intValue());
                    LOG.debug("Can't create container. Reason: \"{}\". Retrying for container \"{}\" with settings - num retries: {}, try: {}, delay: {}", new Object[] {e
                                    .getMessage(), containerName, numRetries, Integer.valueOf(retries), delayInSeconds});
                    Thread.sleep(sleepTime.toMillis());
                }
                else
                {
                    throw e;
                }
            }
        }
    }


    private CloudBlobClient getCloudBlobClient(MediaStorageConfigService.MediaFolderConfig config) throws Exception
    {
        String connectionString = getConnectionString(config);
        CloudStorageAccount account = CloudStorageAccount.parse(connectionString);
        return account.createCloudBlobClient();
    }


    private String getConnectionString(MediaStorageConfigService.MediaFolderConfig config)
    {
        String connectionString = config.getParameter("connection");
        if(connectionString == null)
        {
            throw new ExternalStorageServiceException("Windows Azure specific configuration not found [key: connection was empty");
        }
        return connectionString;
    }


    public File getAsFile(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        throw new UnsupportedOperationException("Obtaining media as file is not supported for Azure Blob storage. Use getMediaAsStream method.");
    }


    public long getSize(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        try
        {
            CloudBlockBlob blob = getBlockBlobReference(config, location);
            blob.downloadAttributes();
            return blob.getProperties().getLength();
        }
        catch(Exception e)
        {
            throw new MediaNotFoundException("Media not found (requested media location: " + location + ")", e);
        }
    }


    public boolean hasValidMediaFolderName(MediaStorageConfigService.MediaFolderConfig config)
    {
        return AzureCloudUtils.hasValidMediaFolderName(config);
    }


    public void onInitialize()
    {
        if(this.cleanOnInit && this.storageConfigService.isStorageStrategyConfigured("windowsAzureBlobStorageStrategy"))
        {
            String defaultStrategyId = this.storageConfigService.getDefaultStrategyId();
            if("windowsAzureBlobStorageStrategy".equals(defaultStrategyId))
            {
                cleanOutContainersForDefaultStorageStrategy(defaultStrategyId);
            }
            cleanOutContainersForFolders(this.storageConfigService.getFolderConfigsForStrategy("windowsAzureBlobStorageStrategy"));
        }
        else
        {
            LOG.warn("Windows Blob storage is not configured properly in properties or clean on init feature is disabled in properties file. Cleaning storage has been skipped.");
        }
    }


    private void cleanOutContainersForDefaultStorageStrategy(String defaultStrategyId)
    {
        MediaStorageConfigService.GlobalMediaStorageConfig globalSettings = this.storageConfigService.getGlobalSettingsForStrategy(defaultStrategyId);
        String connectionString = (String)globalSettings.getParameter("connection", String.class);
        if(StringUtils.isNotBlank(connectionString))
        {
            CloudBlobClient cloudBlobClient = getCloudBlobClientForConnection(connectionString);
            cleanOutContainers(cloudBlobClient);
        }
    }


    private CloudBlobClient getCloudBlobClientForConnection(String connectionString)
    {
        try
        {
            if(GenericValidator.isBlankOrNull(connectionString))
            {
                throw new ExternalStorageServiceException("Connection string to Windows Azure account is blank or null");
            }
            CloudStorageAccount account = CloudStorageAccount.parse(connectionString);
            return account.createCloudBlobClient();
        }
        catch(Exception e)
        {
            throw new ExternalStorageServiceException(e.getMessage(), e);
        }
    }


    private void cleanOutContainersForFolders(Iterable<MediaStorageConfigService.MediaFolderConfig> mediaFolderConfigs)
    {
        for(MediaStorageConfigService.MediaFolderConfig mediaFolderConfig : mediaFolderConfigs)
        {
            try
            {
                CloudBlobClient blobClient = getCloudBlobClient(mediaFolderConfig);
                cleanOutContainers(blobClient);
            }
            catch(Exception e)
            {
                throw new ExternalStorageServiceException(e.getMessage(), e);
            }
        }
    }


    private void cleanOutContainers(CloudBlobClient cloudBlobClient)
    {
        try
        {
            for(CloudBlobContainer container : cloudBlobClient.listContainers())
            {
                container.deleteIfExists();
            }
        }
        catch(StorageException e)
        {
            LOG.error("Cannot clear storage, reason: {}", e.getMessage());
        }
    }


    public void onUpdate()
    {
    }


    public void checkStorageConnection()
    {
        try
        {
            if(this.storageConfigService.isStorageStrategyConfigured("windowsAzureBlobStorageStrategy"))
            {
                MediaStorageConfigService.GlobalMediaStorageConfig settings = this.storageConfigService.getGlobalSettingsForStrategy("windowsAzureBlobStorageStrategy");
                String connectionString = settings.getParameter("connection");
                getCloudBlobClientForConnection(connectionString);
            }
        }
        catch(ExternalStorageServiceException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new ExternalStorageServiceException(e.getMessage(), e);
        }
    }


    public void setCleanOnInit(boolean cleanOnInit)
    {
        this.cleanOnInit = cleanOnInit;
    }


    @Required
    public void setLocationHashService(MediaLocationHashService locationHashService)
    {
        this.locationHashService = locationHashService;
    }


    @Required
    public void setStorageConfigService(MediaStorageConfigService storageConfigService)
    {
        this.storageConfigService = storageConfigService;
    }
}
