package de.hybris.platform.mediaconversion.job;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.mediaconversion.model.job.MediaConversionCronJobModel;
import de.hybris.platform.mediaconversion.util.HybrisRunnable;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Required;

public class MediaConversionJob extends AbstractMediaJob<MediaConversionCronJobModel> implements BeanNameAware, TaskRunner<TaskModel>
{
    private static final Logger LOG = Logger.getLogger(MediaConversionJob.class);
    private TaskService taskService;
    private String beanName;
    private MediaConversionService mediaConversionService;
    private MediaConversionJobDao dao;


    public void handleError(TaskService tServ, TaskModel task, Throwable error)
    {
        LOG.error("Failed to convert media.", error);
    }


    public void run(TaskService taskService, TaskModel task) throws RetryLaterException
    {
        if(task.getContext() instanceof MediaConversionTaskContext)
        {
            MediaConversionTaskContext taskContext = (MediaConversionTaskContext)task.getContext();
            convertSynchronous(taskContext.getCronJob(), taskContext.getContainerPK(), taskContext.getFormatPKs());
        }
        else
        {
            throw new IllegalArgumentException("Invalid task context '" + task.getContext() + "'. Expected '" + MediaConversionTaskContext.class
                            .getName() + "'.");
        }
    }


    public boolean isPerformable()
    {
        return (getDao() != null && getModelService() != null && getMediaConversionService() != null &&
                        getTaskService() != null);
    }


    Queue<Runnable> buildQueue(MediaConversionCronJobModel cronJob)
    {
        Queue<Runnable> ret = new LinkedList<>();
        PK currentContainer = null;
        Set<PK> currentFormats = null;
        for(List<PK> next : (Iterable<List<PK>>)getDao().queryFormatsPerContainerToConvert(cronJob))
        {
            if(next == null || next.size() != 2)
            {
                throw new IllegalArgumentException("Invalid result set entry '" + next + "'.");
            }
            if(!((PK)next.get(0)).equals(currentContainer))
            {
                if(currentContainer != null)
                {
                    ret.add(buildRunnable(cronJob, currentContainer, currentFormats));
                }
                currentContainer = next.get(0);
                currentFormats = new TreeSet<>();
            }
            currentFormats.add(next.get(1));
        }
        if(currentContainer != null)
        {
            ret.add(buildRunnable(cronJob, currentContainer, currentFormats));
        }
        return ret;
    }


    private Runnable buildRunnable(MediaConversionCronJobModel cronJob, PK container, Set<PK> formats)
    {
        return (Runnable)new HybrisRunnable((Runnable)new Object(this, formats, container, cronJob));
    }


    protected void convertSynchronous(MediaConversionCronJobModel cronJob, PK containerPK, Set<PK> formatPks)
    {
        MediaContainerModel container = (MediaContainerModel)getModelService().get(containerPK);
        Set<PK> alreadyProcessed = new TreeSet<>();
        for(PK formatPk : formatPks)
        {
            try
            {
                convert(container, (ConversionMediaFormatModel)getModelService().get(formatPk));
            }
            catch(ModelNotFoundException e)
            {
                LOG.error("Failed to convert container '" + container + "' for format '" + formatPk + "'.", (Throwable)e);
                alreadyProcessed.add(formatPk);
            }
        }
        if(cronJob != null)
        {
            Collection<List<PK>> nowOutdated = getDao().queryOutdatedMedias(cronJob, container);
            boolean thereIsSomeMovement = true;
            while(thereIsSomeMovement && !nowOutdated.isEmpty())
            {
                LOG.debug("" + nowOutdated.size() + " converted media are outdated now. Reconverting them.");
                thereIsSomeMovement = false;
                for(List<PK> row : nowOutdated)
                {
                    PK formatPk = row.get(1);
                    if(alreadyProcessed.contains(formatPk))
                    {
                        LOG.debug("Format " + formatPk + " is blacklisted. Skipping it.");
                        continue;
                    }
                    try
                    {
                        convert(container, (ConversionMediaFormatModel)getModelService().get(formatPk));
                        thereIsSomeMovement = true;
                    }
                    catch(ModelNotFoundException e)
                    {
                        LOG.error("Failed to convert container '" + container + "' for format '" + formatPk + "'.", (Throwable)e);
                    }
                    alreadyProcessed.add(formatPk);
                }
                if(thereIsSomeMovement)
                {
                    nowOutdated = getDao().queryOutdatedMedias(cronJob, container);
                }
            }
        }
    }


    protected void convertAsynchronous(MediaConversionCronJobModel cronJob, PK containerPk, Set<PK> formatPks)
    {
        TaskModel task = (TaskModel)getModelService().create(TaskModel.class);
        task.setContext(new MediaConversionTaskContext(cronJob, containerPk, formatPks));
        task.setRunnerBean(getBeanName());
        getTaskService().scheduleTask(task);
    }


    protected void convert(MediaContainerModel container, ConversionMediaFormatModel format)
    {
        LOG.debug("Deriving format '" + format.getQualifier() + "' for container '" + container.getQualifier() + "'.");
        MediaModel media = getMediaConversionService().getOrConvert(container, (MediaFormatModel)format);
        LOG.debug("Finished '" + media.getCode() + "'.");
        getModelService().detach(media);
    }


    public TaskService getTaskService()
    {
        return this.taskService;
    }


    @Required
    public void setTaskService(TaskService taskService)
    {
        this.taskService = taskService;
    }


    public String getBeanName()
    {
        return this.beanName;
    }


    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
    }


    public MediaConversionService getMediaConversionService()
    {
        return this.mediaConversionService;
    }


    @Required
    public void setMediaConversionService(MediaConversionService service)
    {
        this.mediaConversionService = service;
    }


    public MediaConversionJobDao getDao()
    {
        return this.dao;
    }


    @Required
    public void setDao(MediaConversionJobDao dao)
    {
        this.dao = dao;
    }
}
