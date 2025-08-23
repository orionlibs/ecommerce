package de.hybris.platform.mediaconversion.conversion;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;

public interface ConversionErrorLogStrategy
{
    void logConversionError(MediaContainerModel paramMediaContainerModel, ConversionMediaFormatModel paramConversionMediaFormatModel, MediaModel paramMediaModel, Exception paramException);


    void reportConversionSuccess(MediaContainerModel paramMediaContainerModel, ConversionMediaFormatModel paramConversionMediaFormatModel);
}
