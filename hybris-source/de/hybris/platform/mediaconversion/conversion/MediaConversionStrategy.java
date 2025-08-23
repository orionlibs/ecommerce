package de.hybris.platform.mediaconversion.conversion;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;

public interface MediaConversionStrategy
{
    MediaModel convert(MediaConversionService paramMediaConversionService, MediaModel paramMediaModel, ConversionMediaFormatModel paramConversionMediaFormatModel) throws MediaConversionException;
}
