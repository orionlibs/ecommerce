package de.hybris.platform.mediaconversion.model.attributes;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import org.springframework.beans.factory.annotation.Required;

public class MediaContainerMasterAttributeHandler implements DynamicAttributeHandler<MediaModel, MediaContainerModel>
{
    private MediaConversionService mediaConversionService;


    public MediaModel get(MediaContainerModel model)
    {
        return getMediaConversionService().getMaster(model);
    }


    public void set(MediaContainerModel model, MediaModel value)
    {
        throw new UnsupportedOperationException("Cannot set master media.");
    }


    public MediaConversionService getMediaConversionService()
    {
        return this.mediaConversionService;
    }


    @Required
    public void setMediaConversionService(MediaConversionService service)
    {
        this.mediaConversionService = service;
    }
}
