package de.hybris.platform.amazon.media.storage;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.common.base.Preconditions;
import de.hybris.platform.amazon.media.services.S3StorageServiceFactory;
import de.hybris.platform.core.Registry;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.exceptions.MediaRemovalException;
import de.hybris.platform.media.exceptions.MediaStoreException;
import de.hybris.platform.media.services.MediaHeadersRegistry;
import de.hybris.platform.media.services.MediaLocationHashService;
import de.hybris.platform.media.services.impl.HierarchicalMediaPathBuilder;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.media.storage.impl.StoredMediaData;
import de.hybris.platform.util.MediaUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class S3MediaStorageStrategy implements MediaStorageStrategy
{
    private static final Logger LOG = Logger.getLogger(S3MediaStorageStrategy.class);
    private MediaLocationHashService locationHashService;
    private S3StorageServiceFactory s3StorageServiceFactory;
    private MediaHeadersRegistry mediaHeadersRegistry;
    protected String tenantPrefix;


    public void setTenantPrefix()
    {
        this.tenantPrefix = "sys-" + Registry.getCurrentTenantNoFallback().getTenantID().toLowerCase();
    }


    public StoredMediaData store(MediaStorageConfigService.MediaFolderConfig config, String mediaId, Map<String, Object> metaData, InputStream dataStream)
    {
        Preconditions.checkArgument((config != null), "config is required!");
        Preconditions.checkArgument((mediaId != null), "mediaId is required!");
        Preconditions.checkArgument((metaData != null), "metaData is required!");
        Preconditions.checkArgument((dataStream != null), "dataStream is required!");
        try
        {
            AmazonS3 s3Service = this.s3StorageServiceFactory.getS3ServiceForFolder(config);
            String s3Bucket = this.s3StorageServiceFactory.getS3BucketForFolder(config, s3Service);
            String objectKey = assembleLocation(config, mediaId, (String)metaData.get("fileName"));
            File tempFile = null;
            try
            {
                tempFile = streamToTempFile(mediaId, dataStream);
                s3Service.putObject(createS3Object(s3Bucket, objectKey, metaData, tempFile));
            }
            finally
            {
                FileUtils.deleteQuietly(tempFile);
            }
            ObjectMetadata objectDetails = s3Service.getObjectMetadata(s3Bucket, objectKey);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Storing media object in S3 storage (bucket: " + s3Bucket + ", mediaId: " + mediaId + ")");
            }
            return new StoredMediaData(objectKey, this.locationHashService
                            .createHash(config.getFolderQualifier(), objectKey, objectDetails.getContentLength(), objectDetails
                                            .getContentType()), objectDetails
                            .getContentLength(), objectDetails.getContentType());
        }
        catch(AmazonClientException e)
        {
            throw new MediaStoreException(e);
        }
    }


    private File streamToTempFile(String objectKey, InputStream is)
    {
        File file = null;
        try
        {
            file = File.createTempFile(objectKey, "s3upload");
            FileUtils.copyInputStreamToFile(is, file);
            return file;
        }
        catch(IOException e)
        {
            FileUtils.deleteQuietly(file);
            throw new MediaStoreException(e);
        }
    }


    private String assembleLocation(MediaStorageConfigService.MediaFolderConfig config, String mediaId, String realFileName)
    {
        StringBuilder builder = new StringBuilder(MediaUtil.addTrailingFileSepIfNeeded(this.tenantPrefix));
        HierarchicalMediaPathBuilder pathBuilder = HierarchicalMediaPathBuilder.forDepth(config.getHashingDepth());
        builder.append(pathBuilder.buildPath(config.getFolderQualifier(), mediaId));
        builder.append(mediaId);
        if(StringUtils.isNotBlank(realFileName))
        {
            builder.append(MediaUtil.addLeadingFileSepIfNeeded(realFileName));
        }
        return builder.toString();
    }


    private PutObjectRequest createS3Object(String bucket, String mediaId, Map<String, Object> metaData, File tempFile)
    {
        PutObjectRequest req = new PutObjectRequest(bucket, mediaId, tempFile);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(tempFile.length());
        meta.setContentType((String)metaData.get("mime"));
        addConfiguredHeaders(meta);
        req.setMetadata(meta);
        return req;
    }


    private void addConfiguredHeaders(ObjectMetadata meta)
    {
        for(Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)this.mediaHeadersRegistry.getHeaders().entrySet())
        {
            meta.setHeader(entry.getKey(), entry.getValue());
        }
    }


    public void delete(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        Preconditions.checkArgument((config != null), "config is required!");
        Preconditions.checkArgument((location != null), "location is required!");
        try
        {
            AmazonS3 s3Service = this.s3StorageServiceFactory.getS3ServiceForFolder(config);
            String bucket = this.s3StorageServiceFactory.getS3BucketForFolder(config, s3Service);
            s3Service.deleteObject(bucket, location);
        }
        catch(AmazonClientException e)
        {
            throw new MediaRemovalException("Removal of media: " + location + " has failed.", e);
        }
    }


    public InputStream getAsStream(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        Preconditions.checkArgument((config != null), "config is required!");
        Preconditions.checkArgument((location != null), "location is required!");
        try
        {
            AmazonS3 s3Service = this.s3StorageServiceFactory.getS3ServiceForFolder(config);
            String bucket = this.s3StorageServiceFactory.getS3BucketForFolder(config, s3Service);
            return (InputStream)s3Service.getObject(bucket, location).getObjectContent();
        }
        catch(AmazonClientException e)
        {
            throw new MediaNotFoundException("Media not found (requested media location: " + location + ")", e);
        }
    }


    public File getAsFile(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        throw new UnsupportedOperationException("Obtaining media as file is not supported for S3 storage. Use getMediaAsStream method.");
    }


    public long getSize(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        try
        {
            AmazonS3 s3Service = this.s3StorageServiceFactory.getS3ServiceForFolder(config);
            String bucket = this.s3StorageServiceFactory.getS3BucketForFolder(config, s3Service);
            return s3Service.getObjectMetadata(bucket, location).getContentLength();
        }
        catch(AmazonClientException e)
        {
            throw new MediaNotFoundException("Media not found (requested media location: " + location + ")", e);
        }
    }


    @Required
    public void setLocationHashService(MediaLocationHashService locationHashService)
    {
        this.locationHashService = locationHashService;
    }


    @Required
    public void setS3StorageServiceFactory(S3StorageServiceFactory s3StorageServiceFactory)
    {
        this.s3StorageServiceFactory = s3StorageServiceFactory;
    }


    @Required
    public void setMediaHeadersRegistry(MediaHeadersRegistry mediaHeadersRegistry)
    {
        this.mediaHeadersRegistry = mediaHeadersRegistry;
    }
}
