package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageRegistry;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.media.MediaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class MediaFolderValidator implements ValidateInterceptor
{
    private static final Logger LOG = Logger.getLogger(MediaFolderValidator.class);
    private MediaService mediaService;
    private MediaStorageConfigService mediaStorageConfigService;
    private MediaStorageRegistry mediaStorageRegistry;


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    @Required
    public void setMediaStorageConfigService(MediaStorageConfigService mediaStorageConfigService)
    {
        this.mediaStorageConfigService = mediaStorageConfigService;
    }


    @Required
    public void setMediaStorageRegistry(MediaStorageRegistry mediaStorageRegistry)
    {
        this.mediaStorageRegistry = mediaStorageRegistry;
    }


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof MediaFolderModel)
        {
            String folderQualifier = null;
            try
            {
                folderQualifier = ((MediaFolderModel)model).getQualifier();
                MediaStorageConfigService.MediaFolderConfig mediaFolderConfig = getMediaFolderConfigForFolder(folderQualifier);
                if(isValidationEnabledForGivenFolder(mediaFolderConfig))
                {
                    if(!hasValidMediaFolderName(mediaFolderConfig))
                    {
                        throw new InterceptorException("Media folder with qualifier: " + folderQualifier + " doesn't complies with storage strategy naming convention");
                    }
                }
                MediaFolderModel mediaFolderModel = this.mediaService.getFolder(folderQualifier);
                if(!mediaFolderModel.equals(model) && mediaFolderModel.getQualifier().equals(folderQualifier))
                {
                    throw new InterceptorException("MediaFolder with qualifier: " + folderQualifier + " already exists in the system.", this);
                }
            }
            catch(UnknownIdentifierException uie)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("No media folder found with qualifier " + folderQualifier + ", creating new one");
                }
            }
            catch(AmbiguousIdentifierException aie)
            {
                throw new InterceptorException("More than one mediaFolder with qualifier: " + folderQualifier + " exist in the system.", this);
            }
        }
    }


    protected boolean hasValidMediaFolderName(MediaStorageConfigService.MediaFolderConfig mediaFolderConfig)
    {
        MediaStorageStrategy storageStrategy = this.mediaStorageRegistry.getStorageStrategyForFolder(mediaFolderConfig);
        return storageStrategy.hasValidMediaFolderName(mediaFolderConfig);
    }


    private boolean isValidationEnabledForGivenFolder(MediaStorageConfigService.MediaFolderConfig mediaFolderConfig)
    {
        return mediaFolderConfig.isMediaFolderNameValidationEnabled();
    }


    private MediaStorageConfigService.MediaFolderConfig getMediaFolderConfigForFolder(String folderQualifier)
    {
        return this.mediaStorageConfigService.getConfigForFolder(folderQualifier);
    }
}
