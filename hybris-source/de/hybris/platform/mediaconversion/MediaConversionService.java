package de.hybris.platform.mediaconversion;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.enums.ConversionStatus;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import java.util.Collection;

public interface MediaConversionService
{
    int deleteConvertedMedias(MediaContainerModel paramMediaContainerModel);


    void convertMedias(MediaContainerModel paramMediaContainerModel);


    Collection<MediaModel> getConvertedMedias(MediaContainerModel paramMediaContainerModel);


    Collection<ConversionMediaFormatModel> getAllConversionFormats();


    ConversionStatus getConversionStatus(MediaContainerModel paramMediaContainerModel);


    MediaModel getMaster(MediaContainerModel paramMediaContainerModel);


    MediaModel getOrConvert(MediaContainerModel paramMediaContainerModel, MediaFormatModel paramMediaFormatModel);
}
