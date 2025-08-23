package de.hybris.platform.amazon.media.url;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.common.base.Preconditions;
import de.hybris.platform.amazon.media.services.S3StorageServiceFactory;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.exceptions.ExternalStorageServiceException;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.url.MediaURLStrategy;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

public class S3MediaURLStrategy implements MediaURLStrategy
{
    public static final Logger LOG = Logger.getLogger(S3MediaURLStrategy.class);
    public static final String SIGNED_KEY = "url.signed";
    public static final String SIGNED_VALID_FOR_KEY = "url.signed.validFor";
    private static final Integer DEFAULT_TIME_TO_LIVE = Integer.valueOf(10);
    private static final Boolean DEFAULT_USE_SIGNED = Boolean.TRUE;
    private final S3StorageServiceFactory s3StorageServiceFactory;


    public S3MediaURLStrategy(S3StorageServiceFactory s3StorageServiceFactory)
    {
        this.s3StorageServiceFactory = s3StorageServiceFactory;
    }


    public String getUrlForMedia(MediaStorageConfigService.MediaFolderConfig config, MediaSource media)
    {
        Preconditions.checkArgument((config != null), "Folder config is required to perform this operation");
        Preconditions.checkArgument((media != null), "MediaSource is required to perform this operation");
        String url = "";
        try
        {
            AmazonS3 s3Service = this.s3StorageServiceFactory.getS3ServiceForFolder(config);
            String bucket = this.s3StorageServiceFactory.getS3BucketForFolder(config, s3Service);
            if(isConfiguredForSignedUrls(config))
            {
                url = s3Service.generatePresignedUrl(bucket, media.getLocation(), getTimeToLiveForUrl(config)).toString();
            }
            else
            {
                url = ((AmazonS3Client)s3Service).getUrl(bucket, media.getLocation()).toString();
            }
        }
        catch(ExternalStorageServiceException | com.amazonaws.AmazonClientException e)
        {
            logDebug(media, e);
        }
        return url;
    }


    private boolean isConfiguredForSignedUrls(MediaStorageConfigService.MediaFolderConfig config)
    {
        return ((Boolean)config.getParameter("url.signed", Boolean.class, DEFAULT_USE_SIGNED)).booleanValue();
    }


    private Date getTimeToLiveForUrl(MediaStorageConfigService.MediaFolderConfig config)
    {
        Calendar cal = Calendar.getInstance();
        Integer configuredTimeToLive = (Integer)config.getParameter("url.signed.validFor", Integer.class, DEFAULT_TIME_TO_LIVE);
        cal.add(12, configuredTimeToLive.intValue());
        return cal.getTime();
    }


    private void logDebug(MediaSource media, Exception exc)
    {
        String msg = "Cannot render public url for media location: " + media.getLocation() + " stored in Amazon S3 storage.";
        LOG.error(msg);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(msg, exc);
        }
    }
}
