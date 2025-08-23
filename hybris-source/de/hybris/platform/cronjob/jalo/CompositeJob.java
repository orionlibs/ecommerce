package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class CompositeJob extends GeneratedCompositeJob
{
    private static final Logger LOG = Logger.getLogger(CompositeJob.class.getName());
    private static final int WAIT = 1000;


    protected CronJob.CronJobResult performCronJob(CronJob cronJob) throws AbortCronJobException
    {
        boolean success = true;
        CompositeCronJob compositeCronJob = (CompositeCronJob)cronJob;
        for(CompositeEntry entry : compositeCronJob.getCompositeEntries())
        {
            try
            {
                CronJob executedCronJob = entry.execute();
                while(executedCronJob.isRunning())
                {
                    try
                    {
                        Thread.sleep(1000L);
                    }
                    catch(InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            catch(JaloGenericCreationException e)
            {
                success = false;
                LOG.error(e.getStackTrace());
            }
            catch(JaloAbstractTypeException e)
            {
                success = false;
                LOG.error(e.getStackTrace());
            }
            catch(JaloItemNotFoundException e)
            {
                success = false;
                LOG.error(e.getStackTrace());
            }
        }
        return cronJob.getFinishedResult(success);
    }


    protected boolean canPerform(CronJob cronJob)
    {
        return (cronJob instanceof CompositeCronJob && !((CompositeCronJob)cronJob).getCompositeEntries().isEmpty());
    }
}
