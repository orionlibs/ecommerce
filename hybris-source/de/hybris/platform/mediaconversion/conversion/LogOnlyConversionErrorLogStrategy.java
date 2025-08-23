package de.hybris.platform.mediaconversion.conversion;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import org.apache.log4j.Logger;

public class LogOnlyConversionErrorLogStrategy implements ConversionErrorLogStrategy
{
    private static final Logger LOG = Logger.getLogger(LogOnlyConversionErrorLogStrategy.class);


    public void logConversionError(MediaContainerModel container, ConversionMediaFormatModel targetFormat, MediaModel sourceMedia, Exception fault)
    {
        LOG.error("Failed to convert '" + sourceMedia + "' to format '" + targetFormat + "' for container '" + container + "'.", fault);
    }


    public void reportConversionSuccess(MediaContainerModel container, ConversionMediaFormatModel targetFormat)
    {
    }
}
