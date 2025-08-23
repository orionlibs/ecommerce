package de.hybris.platform.mediaconversion.conversion;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import java.util.Collection;

public interface MediaConversionServiceDao
{
    Collection<ConversionMediaFormatModel> allConversionFormats();


    Collection<MediaModel> getConvertedMedias(MediaContainerModel paramMediaContainerModel);


    MediaModel retrieveMaster(MediaContainerModel paramMediaContainerModel);
}
