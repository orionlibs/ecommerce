package de.hybris.platform.amazon.media.storage;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import de.hybris.platform.amazon.media.services.S3StorageServiceFactory;
import de.hybris.platform.core.Registry;
import de.hybris.platform.media.exceptions.ExternalStorageServiceException;
import de.hybris.platform.media.services.MediaStorageInitializer;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class S3MediaStorageCleaner implements MediaStorageInitializer
{
    private static final Logger LOG = Logger.getLogger(S3MediaStorageCleaner.class);
    private static final String S3_MEDIA_STORAGE_STRATEGY = "s3MediaStorageStrategy";
    private boolean cleanOnInit = true;
    private MediaStorageConfigService storageConfigService;
    private S3StorageServiceFactory s3StorageServiceFactory;
    protected String tenantPrefix;


    public void setTenantPrefix()
    {
        this.tenantPrefix = "sys-" + Registry.getCurrentTenantNoFallback().getTenantID().toLowerCase();
    }


    public void onInitialize()
    {
        if(this.cleanOnInit && this.storageConfigService.isStorageStrategyConfigured("s3MediaStorageStrategy"))
        {
            try
            {
                String defaultStrategyId = this.storageConfigService.getDefaultStrategyId();
                if("s3MediaStorageStrategy".equals(defaultStrategyId))
                {
                    cleanOutFoldersForDefaultStorageStrategy(defaultStrategyId);
                }
                cleanOutBucketsForFolders(this.storageConfigService.getFolderConfigsForStrategy("s3MediaStorageStrategy"));
            }
            catch(ExternalStorageServiceException e)
            {
                logMisconfiguredStorage(e);
            }
        }
        else
        {
            LOG.warn("S3 storage is not configured properly in properties or clean on init feature is disabled in properties file. Cleaning storage has been skipped.");
        }
    }


    private void cleanOutFoldersForDefaultStorageStrategy(String defaultStrategyId)
    {
        MediaStorageConfigService.GlobalMediaStorageConfig globalSettings = this.storageConfigService.getGlobalSettingsForStrategy(defaultStrategyId);
        String globalBucketId = (String)globalSettings.getParameter("bucketId", String.class);
        if(StringUtils.isNotBlank(globalBucketId))
        {
            AmazonS3 service = getS3Service(globalSettings);
            cleanOutBucket(globalBucketId, service);
        }
    }


    private void cleanOutBucketsForFolders(Iterable<MediaStorageConfigService.MediaFolderConfig> folders)
    {
        for(MediaStorageConfigService.MediaFolderConfig folderConfig : folders)
        {
            AmazonS3 service = this.s3StorageServiceFactory.getS3ServiceForFolder(folderConfig);
            String bucket = this.s3StorageServiceFactory.getS3BucketForFolder(folderConfig, service);
            cleanOutBucket(bucket, service);
        }
    }


    private void cleanOutBucket(String bucket, AmazonS3 service)
    {
        try
        {
            List<DeleteObjectsRequest.KeyVersion> keysToDelete = new ArrayList<>();
            ObjectListing objects = service.listObjects(bucket, this.tenantPrefix);
            while(objects != null)
            {
                for(S3ObjectSummary sum : objects.getObjectSummaries())
                {
                    keysToDelete.add(new DeleteObjectsRequest.KeyVersion(sum.getKey()));
                }
                if(objects.isTruncated())
                {
                    objects = service.listNextBatchOfObjects(objects);
                    continue;
                }
                objects = null;
            }
            if(!keysToDelete.isEmpty())
            {
                int deletedIdx = 0;
                while(deletedIdx < keysToDelete.size())
                {
                    int deleteToIdx = Math.min(deletedIdx + 1000, keysToDelete.size());
                    List<DeleteObjectsRequest.KeyVersion> range = keysToDelete.subList(deletedIdx, deleteToIdx);
                    service.deleteObjects((new DeleteObjectsRequest(bucket)).withKeys(range).withQuiet(true));
                    deletedIdx = deleteToIdx;
                }
            }
        }
        catch(AmazonClientException e)
        {
            LOG.error(e.getMessage(), (Throwable)e);
        }
    }


    private void logMisconfiguredStorage(ExternalStorageServiceException exc)
    {
        String msg = "Clean on init feature is enabled but underlying storage problem has occured (reason: " + exc.getMessage() + "). Storage may have configuration problem.";
        LOG.error(msg);
        if(LOG.isDebugEnabled())
        {
            LOG.error(msg, (Throwable)exc);
        }
    }


    public void setCleanOnInit(boolean cleanOnInit)
    {
        this.cleanOnInit = cleanOnInit;
    }


    @Required
    public void setStorageConfigService(MediaStorageConfigService storageConfigService)
    {
        this.storageConfigService = storageConfigService;
    }


    @Required
    public void setS3StorageServiceFactory(S3StorageServiceFactory s3StorageServiceFactory)
    {
        this.s3StorageServiceFactory = s3StorageServiceFactory;
    }


    public void onUpdate()
    {
    }


    public void checkStorageConnection()
    {
        try
        {
            if(this.storageConfigService.isStorageStrategyConfigured("s3MediaStorageStrategy"))
            {
                MediaStorageConfigService.GlobalMediaStorageConfig globalSettings = this.storageConfigService.getGlobalSettingsForStrategy("s3MediaStorageStrategy");
                AmazonS3 s3Service = getS3Service(globalSettings);
                s3Service.getS3AccountOwner();
            }
        }
        catch(Exception e)
        {
            throw new ExternalStorageServiceException(e.getMessage(), e);
        }
    }


    private AmazonS3 getS3Service(MediaStorageConfigService.GlobalMediaStorageConfig globalConfig)
    {
        String accessKey = (String)globalConfig.getParameter("accessKeyId", String.class);
        String secretAccessKey = (String)globalConfig.getParameter("secretAccessKey", String.class);
        String endPoint = (String)globalConfig.getParameter("endpoint", String.class);
        return this.s3StorageServiceFactory.getS3Service(accessKey, secretAccessKey, endPoint);
    }
}
