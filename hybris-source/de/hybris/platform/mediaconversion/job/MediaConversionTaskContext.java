package de.hybris.platform.mediaconversion.job;

import de.hybris.platform.core.PK;
import de.hybris.platform.mediaconversion.model.job.MediaConversionCronJobModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.io.Serializable;
import java.util.Set;

class MediaConversionTaskContext implements Serializable
{
    private static final long serialVersionUID = 621128208666882458L;
    private final MediaConversionCronJobModel cronJob;
    private final PK containerPK;
    private final Set<PK> formatPKs;


    MediaConversionTaskContext(MediaConversionCronJobModel cronJob, PK containerPK, Set<PK> formatPK)
    {
        this.cronJob = cronJob;
        this.containerPK = containerPK;
        ServicesUtil.validateParameterNotNull(this.containerPK, "Container PK must not be null.");
        this.formatPKs = formatPK;
        ServicesUtil.validateParameterNotNull(this.formatPKs, "Media format PK must not be null.");
    }


    MediaConversionCronJobModel getCronJob()
    {
        return this.cronJob;
    }


    PK getContainerPK()
    {
        return this.containerPK;
    }


    Set<PK> getFormatPKs()
    {
        return this.formatPKs;
    }


    public String toString()
    {
        return getClass().getSimpleName() + "(container:" + getClass().getSimpleName() + ", format:" + getContainerPK() + ")";
    }
}
