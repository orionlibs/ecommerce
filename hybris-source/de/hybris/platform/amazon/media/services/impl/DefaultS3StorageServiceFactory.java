package de.hybris.platform.amazon.media.services.impl;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.common.base.Preconditions;
import de.hybris.platform.amazon.media.services.S3StorageServiceFactory;
import de.hybris.platform.media.exceptions.ExternalStorageServiceException;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.util.Config;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang.StringUtils;

public class DefaultS3StorageServiceFactory implements S3StorageServiceFactory
{
    public static final String ACCESS_KEY = "accessKeyId";
    public static final String SECRET_ACCESS_KEY = "secretAccessKey";
    public static final String BUCKET_ID_KEY = "bucketId";
    public static final String ENDPOINT_KEY = "endpoint";
    private final ConcurrentMap<MediaStorageConfigService.MediaFolderConfig, AmazonS3> serviceCache = new ConcurrentHashMap<>();


    public AmazonS3 getS3Service(String accessKey, String secretAccessKey, String endPoint)
    {
        AWSCredentials awsCredentials = getCredentials(accessKey, secretAccessKey);
        AmazonS3Client s3 = new AmazonS3Client(awsCredentials);
        if(StringUtils.isNotBlank(endPoint))
        {
            s3.setEndpoint(endPoint);
        }
        return (AmazonS3)s3;
    }


    public AmazonS3 getS3ServiceForFolder(MediaStorageConfigService.MediaFolderConfig config)
    {
        if(Config.getBoolean("media.globalSettings.s3MediaStorageStrategy.cached.amazon.service", true))
        {
            AmazonS3 service = this.serviceCache.get(config);
            if(service == null)
            {
                String str1 = getAccessKey(config);
                String str2 = getSecretAccessKey(config);
                String str3 = config.getParameter("endpoint");
                service = getS3Service(str1, str2, str3);
                AmazonS3 existing = this.serviceCache.putIfAbsent(config, service);
                if(existing != null)
                {
                    ((AmazonWebServiceClient)service).shutdown();
                    service = existing;
                }
            }
            return service;
        }
        String accessKey = getAccessKey(config);
        String secretAccessKey = getSecretAccessKey(config);
        String endPoint = config.getParameter("endpoint");
        return getS3Service(accessKey, secretAccessKey, endPoint);
    }


    private String getAccessKey(MediaStorageConfigService.MediaFolderConfig config)
    {
        String accessKey = config.getParameter("accessKeyId");
        if(StringUtils.isBlank(accessKey))
        {
            throw new ExternalStorageServiceException("Access Key not found in S3 configuration");
        }
        return accessKey;
    }


    private String getSecretAccessKey(MediaStorageConfigService.MediaFolderConfig config)
    {
        String accessKey = config.getParameter("secretAccessKey");
        if(StringUtils.isBlank(accessKey))
        {
            throw new ExternalStorageServiceException("Secret Access Key not found in S3 configuration");
        }
        return accessKey;
    }


    private AWSCredentials getCredentials(String accessKey, String secretAccessKey)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(accessKey), "Access key is required");
        Preconditions.checkArgument(StringUtils.isNotBlank(secretAccessKey), "Secret access key is required");
        return (AWSCredentials)new BasicAWSCredentials(accessKey, secretAccessKey);
    }


    public String getS3BucketForFolder(MediaStorageConfigService.MediaFolderConfig config, AmazonS3 service)
    {
        return getBucketId(config);
    }


    private String getBucketId(MediaStorageConfigService.MediaFolderConfig config)
    {
        String bucketId = config.getParameter("bucketId");
        if(StringUtils.isBlank(bucketId))
        {
            throw new ExternalStorageServiceException("Bucket ID not found in S3 configuration");
        }
        return bucketId;
    }
}
