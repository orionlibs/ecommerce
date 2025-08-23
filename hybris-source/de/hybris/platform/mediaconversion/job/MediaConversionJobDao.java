package de.hybris.platform.mediaconversion.job;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.mediaconversion.model.job.MediaConversionCronJobModel;
import java.util.Collection;
import java.util.List;

public interface MediaConversionJobDao
{
    Collection<List<PK>> queryFormatsPerContainerToConvert(MediaConversionCronJobModel paramMediaConversionCronJobModel);


    Collection<PK> queryConvertedMedias(MediaConversionCronJobModel paramMediaConversionCronJobModel);


    Collection<List<PK>> queryOutdatedMedias(MediaConversionCronJobModel paramMediaConversionCronJobModel, MediaContainerModel paramMediaContainerModel);
}
