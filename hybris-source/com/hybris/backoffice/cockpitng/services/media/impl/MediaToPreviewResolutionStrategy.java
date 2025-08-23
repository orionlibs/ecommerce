/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.services.media.impl;

import com.hybris.cockpitng.services.media.impl.AbstractPreviewResolutionStrategy;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.servicelayer.media.MediaService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class MediaToPreviewResolutionStrategy extends AbstractPreviewResolutionStrategy<MediaModel>
{
    /**
     * @deprecated since 1808
     */
    @Deprecated(since = "1808", forRemoval = true)
    private MediaStorageConfigService mediaStorageConfigService;
    private MediaService mediaService;


    @Override
    public String resolvePreviewUrl(final MediaModel target)
    {
        final String url = getMediaService().getUrlForMedia(target);
        if(StringUtils.isNotBlank(url))
        {
            return getMediaURL(target);
        }
        return null;
    }


    @Override
    public String resolveMimeType(final MediaModel target)
    {
        return StringUtils.defaultIfBlank(target.getMime(), StringUtils.EMPTY);
    }


    private String getMediaURL(final MediaModel mediaModel)
    {
        return mediaModel.getURL();
    }


    /**
     * @deprecated since 1808, not used anymore
     */
    @Deprecated(since = "1808", forRemoval = true)
    @Required
    public void setMediaStorageConfigService(final MediaStorageConfigService mediaStorageConfigService)
    {
        this.mediaStorageConfigService = mediaStorageConfigService;
    }


    /**
     * @deprecated since 1808, not used anymore
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected MediaStorageConfigService getMediaStorageConfigService()
    {
        return mediaStorageConfigService;
    }


    public MediaService getMediaService()
    {
        return mediaService;
    }


    @Required
    public void setMediaService(final MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
