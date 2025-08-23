package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFolder;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class MoveMediaJob extends GeneratedMoveMediaJob
{
    private static final Logger LOG = Logger.getLogger(MoveMediaJob.class.getName());


    @ForceJALO(reason = "something else")
    public boolean isPerformable(CronJob cronJob)
    {
        if(!(cronJob instanceof MoveMediaCronJob))
        {
            return false;
        }
        return super.isPerformable(cronJob);
    }


    protected CronJob.CronJobResult performCronJob(CronJob cronJob) throws AbortCronJobException
    {
        if(cronJob instanceof MoveMediaCronJob)
        {
            MoveMediaCronJob myCronJob = (MoveMediaCronJob)cronJob;
            MediaFolder newFolder = myCronJob.getTargetFolder();
            boolean success = true;
            for(Media media : myCronJob.getMedias())
            {
                if(myCronJob.isRequestAbortAsPrimitive())
                {
                    cronJob.setRequestAbort(null);
                    throw new AbortCronJobException("abort requested by user");
                }
                try
                {
                    if(media.moveMedia(newFolder))
                    {
                        myCronJob.setMovedMediasCount(myCronJob.getMovedMediasCountAsPrimitive() + 1);
                    }
                }
                catch(JaloBusinessException e)
                {
                    LOG.error(e);
                    success = false;
                }
            }
            return myCronJob.getFinishedResult(success);
        }
        return cronJob.getFinishedResult(false);
    }


    @ForceJALO(reason = "something else")
    public boolean isAbortable(CronJob conJob)
    {
        return true;
    }
}
