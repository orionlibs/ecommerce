package de.hybris.platform.media.impl;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class ForeignDataOwnerHandler implements DynamicAttributeHandler<Collection<MediaModel>, MediaModel>
{
    private MediaService mediaService;


    public Collection<MediaModel> get(MediaModel model)
    {
        return this.mediaService.getMediaWithSameDataReference(model);
    }


    public void set(MediaModel model, Collection<MediaModel> media)
    {
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
