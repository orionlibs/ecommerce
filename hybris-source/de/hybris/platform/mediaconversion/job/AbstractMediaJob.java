package de.hybris.platform.mediaconversion.job;

import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.mediaconversion.model.job.AbstractMediaCronJobModel;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

abstract class AbstractMediaJob<T extends AbstractMediaCronJobModel> implements JobPerformable<T>
{
    private static final Logger LOG = Logger.getLogger(AbstractMediaJob.class);
    private ModelService modelService;


    public PerformResult perform(T cronJob)
    {
        ExceptionCollector eColl = new ExceptionCollector();
        try
        {
            Queue<Runnable> queue = buildQueue(cronJob);
            int size = Math.min((cronJob.getMaxThreads() > 1) ? cronJob.getMaxThreads() : 1, queue.size());
            if(size <= 0)
            {
                LOG.info("Nothing to do.");
            }
            else
            {
                CountDownLatch countDown = new CountDownLatch(size);
                for(int i = 0; i < size; i++)
                {
                    RegistrableThread registrableThread = new RegistrableThread((Runnable)new LatchAwareRunnable(countDown, (Runnable)new QueueWorker(queue, eColl)), getClass().getSimpleName() + "-#" + getClass().getSimpleName());
                    registrableThread.start();
                }
                countDown.await();
            }
        }
        catch(InterruptedException e)
        {
            LOG.error("Failed to await thread to finish conversion.", e);
            return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug(eColl.toString());
        }
        if(eColl.hasExceptions())
        {
            LOG.warn("CronJob finished with errors.");
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    public boolean isAbortable()
    {
        return false;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    abstract Queue<Runnable> buildQueue(T paramT);
}
