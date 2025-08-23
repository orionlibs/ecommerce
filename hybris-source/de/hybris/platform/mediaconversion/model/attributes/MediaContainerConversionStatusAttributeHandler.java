package de.hybris.platform.mediaconversion.model.attributes;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.enums.ConversionStatus;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import org.springframework.beans.factory.annotation.Required;

public class MediaContainerConversionStatusAttributeHandler implements DynamicAttributeHandler<ConversionStatus, MediaContainerModel>
{
    private MediaConversionService mediaConversionService;


    public ConversionStatus get(MediaContainerModel model)
    {
        return getMediaConversionService().getConversionStatus(model);
    }


    public void set(MediaContainerModel model, ConversionStatus value)
    {
        throw new UnsupportedOperationException("Cannot set conversion status.");
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
