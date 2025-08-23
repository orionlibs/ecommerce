package de.hybris.platform.mediaconversion.conversion;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.media.MediaIOException;
import java.io.InputStream;

public interface ConvertedMediaCreationStrategy
{
    MediaModel createOrUpdate(MediaModel paramMediaModel, ConversionMediaFormatModel paramConversionMediaFormatModel, InputStream paramInputStream) throws MediaIOException;
}
