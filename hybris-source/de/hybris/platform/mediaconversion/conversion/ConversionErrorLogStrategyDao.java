package de.hybris.platform.mediaconversion.conversion;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.mediaconversion.model.ConversionErrorLogModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import java.util.Collection;

public interface ConversionErrorLogStrategyDao
{
    Collection<ConversionErrorLogModel> findAllErrorLogs(MediaContainerModel paramMediaContainerModel, ConversionMediaFormatModel paramConversionMediaFormatModel);
}
