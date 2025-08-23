package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import org.apache.log4j.Logger;

public class CompositeEntry extends GeneratedCompositeEntry
{
    private static final Logger log = Logger.getLogger(CompositeEntry.class.getName());


    public CronJob execute() throws JaloGenericCreationException, JaloAbstractTypeException, JaloItemNotFoundException
    {
        return execute(JaloSession.getCurrentSession().getSessionContext());
    }


    public CronJob execute(SessionContext ctx) throws JaloGenericCreationException, JaloAbstractTypeException, JaloItemNotFoundException
    {
        CronJob executableCronJob = getExecutableCronJob(ctx);
        if(executableCronJob != null)
        {
            executableCronJob.getJob(ctx).perform(executableCronJob, true);
        }
        else if(getTriggerableJob(ctx) instanceof TriggerableJob)
        {
            Job job = getTriggerableJob(ctx);
            executableCronJob = job.getCronJobs().iterator().next();
            job.perform(executableCronJob);
        }
        else
        {
            throw new IllegalStateException("Neither a CronJob or a Job instance were assigned!");
        }
        return executableCronJob;
    }


    @ForceJALO(reason = "something else")
    public void setExecutableCronJob(SessionContext ctx, CronJob value)
    {
        if(value.equals(getCompositeCronJob(ctx)))
        {
            throw new IllegalArgumentException("Could not assign the currently used CompositeCronJob instance!");
        }
        if(getTriggerableJob(ctx) == null)
        {
            setProperty(ctx, "executableCronJob", value);
        }
        else
        {
            throw new IllegalArgumentException("Executable Job already assigned!");
        }
    }


    @ForceJALO(reason = "something else")
    public void setTriggerableJob(SessionContext ctx, Job value)
    {
        if(value instanceof TriggerableJob || value instanceof ServicelayerJob)
        {
            if(getExecutableCronJob() == null)
            {
                setProperty(ctx, "triggerableJob", value);
            }
            else
            {
                throw new IllegalArgumentException("Executable CronJob already assigned!");
            }
        }
        else
        {
            throw new IllegalArgumentException("Assigned Job either does not implement " + TriggerableJob.class.getName() + " or is not a " + ServicelayerJob.class
                            .getName());
        }
    }
}
