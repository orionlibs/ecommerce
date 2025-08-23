package de.hybris.platform.servicelayer.media;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import org.springframework.beans.factory.annotation.Required;

public class DynamicAttributesMediaUrl implements DynamicAttributeHandler<String, MediaModel>
{
    private MediaService mediaService;


    public String get(MediaModel model)
    {
        return this.mediaService.getUrlForMedia(model);
    }


    public void set(MediaModel model, String value)
    {
        this.mediaService.setUrlForMedia(model, value);
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
