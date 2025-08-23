package de.hybris.platform.amazon.media.services;

import com.amazonaws.services.s3.AmazonS3;
import de.hybris.platform.media.storage.MediaStorageConfigService;

public interface S3StorageServiceFactory
{
    AmazonS3 getS3Service(String paramString1, String paramString2, String paramString3);


    AmazonS3 getS3ServiceForFolder(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig);


    String getS3BucketForFolder(MediaStorageConfigService.MediaFolderConfig paramMediaFolderConfig, AmazonS3 paramAmazonS3);
}
