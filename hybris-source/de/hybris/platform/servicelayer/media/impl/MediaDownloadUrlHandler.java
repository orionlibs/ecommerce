package de.hybris.platform.servicelayer.media.impl;

import de.hybris.platform.core.model.media.AbstractMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import org.springframework.beans.factory.annotation.Required;

public class MediaDownloadUrlHandler implements DynamicAttributeHandler<String, MediaModel>
{
    private MediaService mediaService;


    public String get(MediaModel model)
    {
        String folderQualifier = (model.getFolder() != null) ? model.getFolder().getQualifier() : this.mediaService.getRootFolder().getQualifier();
        return MediaManager.getInstance().getDownloadURLForMedia(folderQualifier, (MediaSource)new ModelMediaSource((AbstractMediaModel)model));
    }


    public void set(MediaModel model, String s)
    {
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
