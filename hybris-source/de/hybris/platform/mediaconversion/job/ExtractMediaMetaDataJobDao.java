package de.hybris.platform.mediaconversion.job;

import de.hybris.platform.core.PK;
import de.hybris.platform.mediaconversion.model.job.ExtractMediaMetaDataCronJobModel;
import java.util.Collection;

public interface ExtractMediaMetaDataJobDao
{
    Collection<PK> findMetaDataUpdates(ExtractMediaMetaDataCronJobModel paramExtractMediaMetaDataCronJobModel);
}
